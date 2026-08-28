# 동시성 처리 정책

배송지(shipping_address)·소셜 credential 도메인의 동시성 처리 방식과 그 근거를 정리합니다.

---

## 1. 전제

- Spring Session 기반 인증이며, **동일 회원이 여러 기기에서 동시에 로그인·요청** 가능
- PostgreSQL 기본 격리 수준은 `READ COMMITTED`
- `uq_shipping_address_default` partial unique index로 회원당 default 배송지는 최대 1개로 DB가 보장

```sql
CREATE UNIQUE INDEX uq_shipping_address_default
    ON shipping_address (member_id) WHERE is_default = true;
```

---

## 2. 각 작업의 동시성 처리 방식

| 작업 | 처리 방식 | 근거 |
|---|---|---|
| `create` | **Advisory Lock** (`shipping:create:{memberId}`) | 5개 제한은 count 기반이라 SQL WHERE로 phantom read 방지 불가 |
| `markAsDefault` | **조건부 UPDATE + rows affected 검사** | Row-level 락으로 충분 |
| `delete` | **조건부 DELETE + 후속 UPDATE (`promoteOldestIfNoDefault`)** | Row-level 락 + idempotent 후처리 |
| `edit` | JPA dirty check | 자기 자신 필드만 수정, 경쟁 상태 없음 |

---

## 3. `create` — 왜 Advisory Lock인가

**요구사항**: 회원당 배송지 5개까지만 허용

**시도한 방안: SQL WHERE**
```sql
INSERT INTO shipping_address (...)
SELECT ...
WHERE (SELECT COUNT(*) FROM shipping_address WHERE member_id = :memberId) < 5;
```

**실패 이유**: `READ COMMITTED`에서 각 statement는 최신 커밋 스냅샷만 볼 뿐, 다른 트랜잭션의 미커밋 INSERT는 보이지 않는다.

```
T1: count=4 확인 → INSERT (미커밋)
T2: count=4 확인 (T1 미커밋) → INSERT
둘 다 커밋 → 6개 존재 ❌
```

Count 기반 제약은 row 단위 락으로 phantom read를 막을 수 없다.

**대안 비교**
| 방안 | 문제점 |
|---|---|
| SERIALIZABLE 격리 | 재시도 로직 필수, 트랜잭션 전체가 SSI 대상이 되어 오버헤드 큼 |
| `SELECT ... FOR UPDATE` on member row | member 테이블에 부작용, 락 범위가 배송지 도메인을 벗어남 |
| **Advisory Lock (`shipping:create:{memberId}`)** | 회원 단위로 create만 직렬화. 명시적이고 targeted |

**락 특성**
- `pg_advisory_xact_lock` — 트랜잭션 종료 시 자동 해제
- `lock_timeout = 3s` — 무한 대기 방지, 초과 시 `CannotAcquireLockException` → 409 반환
- 트랜잭션이 짧아(수 ms) 사용자 체감 지연은 무시 수준

---

## 4. `markAsDefault` — SQL 조건절로 충분한 이유

**구현**
```java
@Transactional
public void markAsDefault(Long memberId, Long addressId) {
    ShippingAddress target = loadOwned(memberId, addressId);
    if (target.isDefault()) return;
    shippingAddressRepository.unmarkDefaultExcept(memberId, addressId);
    int updated = shippingAddressRepository.setAsDefault(addressId, memberId);
    if (updated == 0) {
        throw new BusinessException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND);
    }
}
```

**동시성 시나리오 분석**

### 4-1. `markAsDefault(A) + delete(A)` 동시 실행
```
T1: unmarkDefaultExcept(memberId, A) — 현재 default(B) row lock 획득, 해제
T2: DELETE A — A 행에 대해 lock 대기 (T1이 setAsDefault에서 A 잠금 대기 중)
T1: setAsDefault(A) — A row lock 획득, is_default=true, commit
T2: DELETE 진행 → 1 row deleted
T2: promoteOldestIfNoDefault → default 없으므로 최고령 promotion
```
✅ 최종적으로 default가 유지됨.

### 4-2. `markAsDefault(A) + markAsDefault(B)` 동시 실행
```
T1: unmarkDefaultExcept(memberId, A) — 현재 default(C) 잠금, 해제
T2: unmarkDefaultExcept(memberId, B) — C 잠금 대기
T1: setAsDefault(A), commit
T2: unmark 재평가 — A가 default (id != B) → A 해제
T2: setAsDefault(B), commit
```
✅ 마지막 요청(B) 승리. 최종 default는 1개.

### 4-3. 최악의 경우 `uq_shipping_address_default` 위반
매우 드물게 두 트랜잭션이 각각 다른 row를 is_default=true로 만들려 하면 unique index 위반이 발생. `GlobalExceptionHandler`의 `DataIntegrityViolationException` 핸들러가 `SHIPPING_ADDRESS_DEFAULT_CONFLICT` (409)로 응답한다.

### 4-4. 삭제된 대상을 default로 지정
`setAsDefault`의 rows affected = 0을 검사하여 `SHIPPING_ADDRESS_NOT_FOUND` 반환. 이 경우 이미 실행된 `unmarkDefaultExcept`는 트랜잭션 롤백으로 함께 취소된다.

---

## 5. `delete` — SQL 조건절로 충분한 이유

**구현**
```java
@Transactional
public void delete(Long memberId, Long addressId) {
    loadOwned(memberId, addressId);
    int deleted = shippingAddressRepository.deleteByIdAndMemberId(addressId, memberId);
    if (deleted == 0) {
        throw new BusinessException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND);
    }
    shippingAddressRepository.promoteOldestIfNoDefault(memberId);
}
```

**핵심 아이디어**: 삭제 대상이 default였는지 여부와 무관하게, 삭제 후 "default가 없다면 최고령을 승격"하는 idempotent 후처리를 실행한다.

**`promoteOldestIfNoDefault` 쿼리**
```sql
UPDATE shipping_address SET is_default = true
WHERE id = (
    SELECT id FROM shipping_address
    WHERE member_id = :memberId
    ORDER BY created_at ASC LIMIT 1
)
  AND NOT EXISTS (
    SELECT 1 FROM shipping_address
    WHERE member_id = :memberId AND is_default = true
  );
```

- `NOT EXISTS`로 이미 default가 있으면 no-op → 여러 번 호출해도 안전
- 남은 주소가 0개면 서브쿼리가 비어 no-op

**동시성 시나리오 분석**

### 5-1. `delete(A default) + delete(B non-default)` 동시 실행
- T1: DELETE A — commit
- T2: DELETE B — commit
- T1: promoteOldestIfNoDefault → default 없음 → 최고령 승격
- T2: promoteOldestIfNoDefault → default 존재 → no-op

✅ 어느 순서든 default 1개가 유지됨.

### 5-2. `delete(A) + delete(A)` 동시 실행
- 첫 번째: `deleteByIdAndMemberId` → 1 row deleted
- 두 번째: 이미 삭제됨 → 0 rows deleted → `SHIPPING_ADDRESS_NOT_FOUND` (404)

### 5-3. 승격 대상이 다른 트랜잭션에 의해 삭제되는 극단 케이스
```
T1: DELETE A (default), commit
T1: promoteOldestIfNoDefault → 최고령 B 선택, B에 UPDATE 락 대기 (T2 대기)
T2: DELETE B — 이미 T1이 UPDATE 락 소유 시 대기
```
Row-level 락으로 대체로 직렬화되지만, 특정 순서에서는 T1의 promotion 대상이 사라져 UPDATE가 0 rows에 적용되고 최종적으로 default가 없는 상태가 될 수 있음.

**대응**: MVP에서는 발생 확률이 낮아 허용. 필요 시 재시도 루프 추가.

---

## 6. `edit`

자기 자신 필드(alias/recipient/phone 등)만 수정하며, 다른 배송지나 default 상태를 건드리지 않아 동시성 이슈가 없다. JPA dirty check로 처리.

---

## 7. 소셜 credential — `unlink`

**요구사항**: 회원은 로그인 수단(로컬 비밀번호 + 소셜)이 최소 1개 이상 유지되어야 함. 마지막 credential 해제는 `LAST_CREDENTIAL_CANNOT_UNLINK` (400).

**처리 방식**: **Advisory Lock** (`credential:write:{memberId}`)

**시도한 방안: 검사 후 삭제 (락 없이)**
```java
long socialCount = socialCredentialRepository.countByMemberId(memberId);
boolean hasLocal = localCredentialRepository.existsByMemberId(memberId);
int remaining = (int) socialCount - 1 + (hasLocal ? 1 : 0);
if (remaining < 1) throw new BusinessException(LAST_CREDENTIAL_CANNOT_UNLINK);
socialCredentialRepository.delete(target);
```

**실패 시나리오**: 회원이 google + kakao만 가진 상태에서
```
T1: count=2, hasLocal=false → remaining=1 → 통과 → google 삭제 (미커밋)
T2: count=2 (T1 미커밋), hasLocal=false → remaining=1 → 통과 → kakao 삭제
둘 다 커밋 → credential 0개 → 로그인 불가 ❌
```

배송지 5개 제한과 동일한 count 기반 phantom read. **결과가 계정 접근 상실이라 배송지보다 더 심각.**

**대안 비교**
| 방안 | 문제점 |
|---|---|
| `pg_try_advisory_xact_lock` | 실패 시 409 반환 → 재시도해도 결국 400 뜨는 두 단계 UX |
| SQL 조건절 | count 기반 제약은 WHERE로 phantom read 방지 불가 (배송지 create와 동일) |
| **`pg_advisory_xact_lock` + `lock_timeout=3s`** | 짧은 대기 후 정확한 비즈니스 에러(LAST_CREDENTIAL_CANNOT_UNLINK) 반환 |

**락을 걸지 않는 관련 작업**
| 작업 | 이유 |
|---|---|
| `link` | credential 개수가 감소하지 않아 "0개" 위험 없음 |
| `PasswordChangeService` | 같은 row UPDATE, row-level 락으로 충분, 개수 변동 없음 |
| `withdraw` | 어차피 모든 credential 삭제가 목적. `unlink`와 경합해도 end state 동일 |

---

## 8. 결론 요약

- **Advisory Lock은 count 기반 제약에만 사용한다.** (배송지 5개 제한, credential 최소 1개 유지)
- **default 정합성은 SQL 조건절과 partial unique index로 보장한다.**
  - `unmarkDefaultExcept` / `setAsDefault` / `promoteOldestIfNoDefault` 조합
  - `uq_shipping_address_default`가 최후의 안전망
- **rows affected = 0을 활용해 "동시 삭제된 대상" 등의 예외 케이스를 감지한다.**

이 방향은 락 범위를 최소화하면서도 DB가 제공하는 격리 수준·유니크 제약을 최대한 활용하는 실용적 절충점이다.
