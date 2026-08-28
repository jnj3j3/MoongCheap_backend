# Status 값 정리

> **Java enum**: 코드에 `enum` 클래스가 존재합니다.
> **DB only**: DDL CHECK 제약으로만 정의되어 있으며 아직 Java enum 클래스가 없습니다.

## SellerStatus
> **Java enum** `com.moongcheap_backend.member.domain.SellerStatus`
> 판매자 계정의 심사·운영 상태를 나타냅니다.

| 값 | 설명 |
|---|---|
| `PENDING` | 판매자 등록 신청 후 관리자 심사 대기 중 |
| `APPROVED` | 심사 완료, 정상 영업 가능 상태 |
| `BLOCKED` | 관리자에 의해 판매 차단된 상태 |
| `WITHDRAWN` | 판매자 스스로 탈퇴한 상태 |

**상태 전이**
```
PENDING → APPROVED   (관리자 승인)
APPROVED → BLOCKED   (관리자 차단)
APPROVED → WITHDRAWN (판매자 탈퇴 → softDelete())
```

---

## ProductCatalogStatus
> **Java enum** `com.moongcheap_backend.product.domain.productCatalog.ProductCatalogStatus`
> `product_catalog.status VARCHAR(20)`
> 상품 도감의 노출·수요 접수 가능 여부를 나타냅니다.

| 값 | 설명 |
|---|---|
| `ACTIVE` | 도감 노출 및 수요 접수 가능 (기본값) |
| `INACTIVE` | 노출 중단 |

---

## DemandStatus
> **DB only** `demand.status VARCHAR(20)`
> 구매자 수요 요청 1건의 생애주기를 나타냅니다.

| 값 | 진행 중 여부 | 설명 |
|---|:---:|---|
| `UNASSIGNED` | O | 미배정 — 클러스터 편입 대기 (최대 2일) |
| `SUBSTITUTE_OFFERED` | O | 대체상품 제안 — 유사 클러스터 편입 제안에 대한 응답 대기 |
| `ASSIGNED` | O | 배정 완료 — 수요보드 편입, 진행 중 |
| `PAYMENT_PENDING` | O | 낙찰 확정 — 본인 48시간 결제 대기 |
| `CLOSED` | | 종료 — 본인 결제 완료로 요청 종결 (공구 전체 성립 여부와 무관) |
| `CANCELED` | | 사용자 취소 |
| `EXPIRED` | | 소멸 — 미배정 2일 경과, 클러스터 생성 실패, 제안 무응답 |
| `DELETED` | | 운영자·시스템 무효화 |

> `UNASSIGNED`, `SUBSTITUTE_OFFERED`, `ASSIGNED`, `PAYMENT_PENDING` 4종을 "진행 중"으로 간주하며, 이 상태의 회원·도감 조합에 중복 접수 UNIQUE 제약이 적용됩니다.

**상태 전이**
```
UNASSIGNED → SUBSTITUTE_OFFERED          (대체상품 제안 수신)
UNASSIGNED → ASSIGNED                    (클러스터 편입)
UNASSIGNED → EXPIRED                     (2일 초과 미배정)
SUBSTITUTE_OFFERED → ASSIGNED            (제안 수락)
SUBSTITUTE_OFFERED → EXPIRED             (제안 무응답·거절)
ASSIGNED → PAYMENT_PENDING              (낙찰 확정)
PAYMENT_PENDING → CLOSED                (본인 결제 완료)
ASSIGNED / UNASSIGNED → CANCELED        (사용자 취소, MVP 확정 대기)
```

---

## ProductStatus
> **DB only** `product.status VARCHAR(20)`
> 판매자 응찰의 상태를 나타냅니다. 판매자는 한 수요보드에 유효 상태 응찰을 1건만 보유할 수 있습니다.

| 값 | 설명 |
|---|---|
| `BIDDING` | 응찰 — 낙찰 판정 대기 (기본값) |
| `AWARDED` | 낙찰 — 해당 보드의 최종 낙찰 응찰 |
| `ON_SALE` | 판매 중 — 여분 즉시판매 |
| `SOLD_OUT` | 품절 |

**상태 전이**
```
BIDDING → AWARDED   (낙찰 판정)
AWARDED → ON_SALE   (여분 판매 개시)
ON_SALE → SOLD_OUT  (재고 소진)
```

---

## DemandBoardStatus
> **DB only** `demand_board.status VARCHAR(20)`
> 수요 클러스터(공구)의 진행 상태를 나타냅니다.
>
| 값 (기능정의서 기준) | 설명 |
|---|---|
| `GB_GATHERING` | 모이는 중 — 응찰 접수 및 참여자 모집 |
| `GB_ACTION_REQUIRED` | 확인 필요 — 낙찰 확정, 참여자별 48시간 결제 확인 대기 |
| `GB_CLOSED` | 종료 — 전원 결제 완료, 주문 생성 |
| `GB_CANCELED` | 취소 — MVP에서는 도달하지 않음 |
