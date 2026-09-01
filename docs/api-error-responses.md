# API 실패 응답 규격

## 공통 응답 형식

```json
{
  "code": "에러코드",
  "message": "에러 메시지",
  "fieldErrors": [
    { "field": "필드명", "message": "필드 에러 메시지" }
  ]
}
```

> `fieldErrors`는 400 유효성 검사 실패 시에만 포함됩니다. 나머지는 빈 배열 `[]`입니다.

---

## 공통 에러 (전 엔드포인트)

| HTTP | code | message |
|------|------|---------|
| 400 | COMMON_400 | 입력값이 올바르지 않습니다. |
| 401 | COMMON_401 | 인증이 필요합니다. |
| 403 | COMMON_403 | 접근 권한이 없습니다. |
| 500 | COMMON_500 | 서버 오류가 발생했습니다. |
| 409 | COMMON_409 | 요청이 충돌했습니다. 잠시 후 다시 시도해주세요. |

---

## 엔드포인트별 비즈니스 에러

### Auth

#### `POST /api/auth/signup` — 아이디 회원가입
| HTTP | code | message |
|------|------|---------|
| 400 | AUTH_003 | 비밀번호 형식이 올바르지 않습니다. |
| 400 | AUTH_004 | 비밀번호가 일치하지 않습니다. |
| 400 | AUTH_005 | 비밀번호에 아이디를 포함할 수 없습니다. |
| 409 | AUTH_001 | 이미 사용 중인 아이디입니다. |
| 409 | AUTH_015 | 회원가입에 실패했습니다. 잠시 후 다시 시도해주세요. (동시성 충돌) |

#### `POST /api/auth/login` — 아이디 로그인
| HTTP | code | message |
|------|------|---------|
| 401 | AUTH_006 | 아이디 또는 비밀번호가 올바르지 않습니다. |
| 423 | AUTH_007 | 연속 로그인 실패로 계정이 잠겼습니다. 잠시 후 다시 시도해주세요. |

#### `PATCH /api/auth/password` — 비밀번호 변경
| HTTP | code | message |
|------|------|---------|
| 400 | AUTH_004 | 비밀번호가 일치하지 않습니다. |
| 400 | AUTH_008 | 비밀번호가 등록된 회원만 사용할 수 있습니다. |
| 400 | AUTH_009 | 이전과 동일한 비밀번호는 사용할 수 없습니다. |
| 401 | AUTH_006 | 아이디 또는 비밀번호가 올바르지 않습니다. |

#### `DELETE /api/auth/withdraw` — 회원 탈퇴
| HTTP | code | message |
|------|------|---------|
| 400 | AUTH_003 | 비밀번호 형식이 올바르지 않습니다. |
| 401 | AUTH_006 | 아이디 또는 비밀번호가 올바르지 않습니다. |
| 400 | AUTH_014 | 진행 중인 거래가 있어 탈퇴할 수 없습니다. |

#### `POST /api/sellers` — 판매자 등록
| HTTP | code | message |
|------|------|---------|
| 400 | SELLER_002 | 사업자등록번호 형식이 올바르지 않습니다. |
| 409 | SELLER_001 | 이미 판매자로 등록되어 있습니다. |
| 409 | SELLER_003 | 이미 등록된 사업자등록번호입니다. |

#### `DELETE /api/auth/social/{provider}` — 소셜 계정 연동 해제
| HTTP | code | message |
|------|------|---------|
| 400 | AUTH_012 | 마지막 로그인 수단은 해제할 수 없습니다. |
| 409 | AUTH_011 | 이미 다른 회원에 연동된 소셜 계정입니다. |

---

### Member

#### `PATCH /api/members/me/profile` — 프로필 수정
| HTTP | code | message |
|------|------|---------|
| 400 | USER_003 | 닉네임 형식이 올바르지 않습니다. |
| 409 | USER_002 | 이미 사용 중인 닉네임입니다. |

#### `GET /api/members/{id}/public` — 판매자 공개 정보 조회
| HTTP | code | message |
|------|------|---------|
| 404 | SELLER_004 | 판매자 정보를 찾을 수 없습니다. |

### Shipping Address

#### `GET /api/shipping-addresses/{id}` — 배송지 상세
| HTTP | code | message |
|------|------|---------|
| 404 | SHIP_001 | 배송지를 찾을 수 없습니다. |
| 403 | SHIP_003 | 본인 소유의 배송지가 아닙니다. |

#### `POST /api/shipping-addresses` — 배송지 등록
| HTTP | code | message |
|------|------|---------|
| 400 | SHIP_002 | 배송지는 최대 5개까지 등록할 수 있습니다. |

#### `PATCH /api/shipping-addresses/{id}` — 배송지 수정
| HTTP | code | message |
|------|------|---------|
| 404 | SHIP_001 | 배송지를 찾을 수 없습니다. |
| 403 | SHIP_003 | 본인 소유의 배송지가 아닙니다. |

#### `DELETE /api/shipping-addresses/{id}` — 배송지 삭제
| HTTP | code | message |
|------|------|---------|
| 404 | SHIP_001 | 배송지를 찾을 수 없습니다. |
| 403 | SHIP_003 | 본인 소유의 배송지가 아닙니다. |

#### `PATCH /api/shipping-addresses/{id}/default` — 기본 배송지 지정
| HTTP | code | message |
|------|------|---------|
| 404 | SHIP_001 | 배송지를 찾을 수 없습니다. |
| 409 | SHIP_004 | 기본 배송지 변경이 충돌했습니다. 다시 시도해주세요. |

---

### Notification

#### `PATCH /api/notifications/settings/{type}` — 알림 설정 변경
| HTTP | code | message |
|------|------|---------|
| 400 | COMMON_400 | 필수 알림은 해제할 수 없습니다. |

---

### Demand

#### `POST /api/members/me/demand` — 수요 등록
| HTTP | code | message |
|------|------|---------|
| 404 | PRODUCT_001 | 상품 카탈로그를 찾을 수 없습니다. |
| 409 | DEMAND_001 | 이미 진행 중인 수요 요청이 있습니다. |
