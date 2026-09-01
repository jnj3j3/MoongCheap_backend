/* ============================================================================
 * Migration: demand.status 에 PAYMENT_PENDING 추가
 * ----------------------------------------------------------------------------
 * 변경 요약
 *   - PAYMENT_PENDING(낙찰 확정, 본인 48시간 결제 대기) 상태를 신규 추가
 *   - PAID 는 별도로 두지 않고 CLOSED 로 흡수 (CLOSED = 본인 결제 완료 = 요청 종결)
 *   - CLOSED 의 의미를 "보드 GB_CLOSED 도달"에서 "본인 결제 완료"로 재정의
 *
 * 대상 3곳
 *   1) CHECK  ck_demand_status               — PAYMENT_PENDING 허용값 추가
 *   2) UNIQUE uq_demand_member_catalog_active — 진행 중 판정에 PAYMENT_PENDING 포함
 *                                               (CLOSED 는 종결이므로 제외 유지)
 *   3) COMMENT demand.status                  — 상태 설명 갱신
 *
 * 상태 흐름
 *   ASSIGNED ──낙찰 확정──> PAYMENT_PENDING ──본인 결제 완료──> CLOSED
 * ========================================================================== */

BEGIN;

-- 1) CHECK 제약: PAYMENT_PENDING 추가
ALTER TABLE "demand" DROP CONSTRAINT "ck_demand_status";

ALTER TABLE "demand" ADD CONSTRAINT "ck_demand_status"
    CHECK ("status" IN (
                        'UNASSIGNED',
                        'SUBSTITUTE_OFFERED',
                        'ASSIGNED',
                        'PAYMENT_PENDING',
                        'CLOSED',
                        'CANCELED',
                        'EXPIRED',
                        'DELETED'
        ));

-- 2) 중복 접수 방지 UNIQUE: 진행 중 상태에 PAYMENT_PENDING 포함
--    CLOSED 는 결제까지 끝난 종결 상태이므로 동일 상품 재접수를 위해 제외 유지
DROP INDEX "uq_demand_member_catalog_active";

CREATE UNIQUE INDEX "uq_demand_member_catalog_active"
    ON "demand" ("member_id", "catalog_id")
    WHERE "status" IN ('UNASSIGNED', 'SUBSTITUTE_OFFERED', 'ASSIGNED', 'PAYMENT_PENDING');

-- 3) COMMENT 갱신
COMMENT ON COLUMN "demand"."status" IS 'UNASSIGNED(미배정 — 클러스터 편입 대기, 최대 2일) / SUBSTITUTE_OFFERED(대체상품 제안 — 유사 상품 클러스터 편입 제안에 대한 응답 대기) / ASSIGNED(배정완료 — 수요보드 편입, 진행 중) / PAYMENT_PENDING(낙찰 확정 — 본인 48시간 결제 대기) / CLOSED(종료 — 본인 결제 완료로 요청 종결. 공구 전체 성립 여부와 무관) / CANCELED(사용자 취소 — 참여 취소 MVP 포함 여부 확정 대기) / EXPIRED(소멸 — 미배정 2일 경과, 클러스터 생성 실패, 제안 무응답) / DELETED(운영자·시스템 무효화). UNASSIGNED·SUBSTITUTE_OFFERED·ASSIGNED·PAYMENT_PENDING 4종을 진행 중으로 보며 중복 접수 UNIQUE 대상이다';

COMMIT;


/* ---------------------------------------------------------------------------
 * ROLLBACK (필요 시 수동 실행)
 * ---------------------------------------------------------------------------
BEGIN;

ALTER TABLE "demand" DROP CONSTRAINT "ck_demand_status";
ALTER TABLE "demand" ADD CONSTRAINT "ck_demand_status"
    CHECK ("status" IN (
        'UNASSIGNED','SUBSTITUTE_OFFERED','ASSIGNED',
        'CLOSED','CANCELED','EXPIRED','DELETED'
    ));

DROP INDEX "uq_demand_member_catalog_active";
CREATE UNIQUE INDEX "uq_demand_member_catalog_active"
    ON "demand" ("member_id", "catalog_id")
    WHERE "status" IN ('UNASSIGNED', 'SUBSTITUTE_OFFERED', 'ASSIGNED');

COMMENT ON COLUMN "demand"."status" IS 'UNASSIGNED(미배정 — 클러스터 편입 대기, 최대 2일) / SUBSTITUTE_OFFERED(대체상품 제안 — 유사 상품 클러스터 편입 제안에 대한 응답 대기) / ASSIGNED(배정완료 — 수요보드 편입, 진행 중) / CLOSED(종료 — 소속 보드가 GB_CLOSED 도달, 주문 생성 완료) / CANCELED(사용자 취소 — 참여 취소 MVP 포함 여부 확정 대기) / EXPIRED(소멸 — 미배정 2일 경과, 클러스터 생성 실패, 제안 무응답) / DELETED(운영자·시스템 무효화). UNASSIGNED·SUBSTITUTE_OFFERED·ASSIGNED 3종만 진행 중으로 보며 중복 접수 UNIQUE 대상이다';

COMMIT;
 * ------------------------------------------------------------------------- */