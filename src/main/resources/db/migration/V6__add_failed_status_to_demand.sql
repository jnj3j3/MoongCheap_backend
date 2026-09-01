/* ============================================================================
 * Migration: demand.status 에 FAILED 추가
 * ----------------------------------------------------------------------------
 * 변경 요약
 *   - FAILED(성사 실패 — 소속 보드 미낙찰 또는 최소 수량 미달, 시스템 처리) 상태 신규 추가
 *   - 종료 상태이므로 uq_demand_member_catalog_active 대상에서 제외 (변경 없음)
 *
 * 대상 2곳
 *   1) CHECK  ck_demand_status  — FAILED 허용값 추가
 *   2) COMMENT demand.status    — 상태 설명 갱신
 *
 * 상태 흐름
 *   ASSIGNED ──미낙찰/최소 수량 미달──> FAILED
 * ========================================================================== */

BEGIN;

-- 1) CHECK 제약: FAILED 추가
ALTER TABLE "demand" DROP CONSTRAINT "ck_demand_status";

ALTER TABLE "demand" ADD CONSTRAINT "ck_demand_status"
    CHECK ("status" IN (
        'UNASSIGNED',
        'SUBSTITUTE_OFFERED',
        'ASSIGNED',
        'PAYMENT_PENDING',
        'CLOSED',
        'FAILED',
        'CANCELED',
        'EXPIRED',
        'DELETED'
    ));

-- 2) COMMENT 갱신
COMMENT ON COLUMN "demand"."status" IS 'UNASSIGNED(미배정 — 클러스터 편입 대기, 최대 2일) / SUBSTITUTE_OFFERED(대체상품 제안 — 유사 상품 클러스터 편입 제안에 대한 응답 대기) / ASSIGNED(배정완료 — 수요보드 편입, 진행 중) / PAYMENT_PENDING(낙찰 확정 — 본인 48시간 결제 대기) / CLOSED(종료 — 본인 결제 완료로 요청 종결. 공구 전체 성립 여부와 무관) / FAILED(성사 실패 — 소속 보드 미낙찰 또는 최소 수량 미달, 시스템 처리) / CANCELED(사용자 취소 — 참여 취소 MVP 포함 여부 확정 대기) / EXPIRED(소멸 — 미배정 2일 경과, 클러스터 생성 실패, 제안 무응답) / DELETED(운영자·시스템 무효화). UNASSIGNED·SUBSTITUTE_OFFERED·ASSIGNED·PAYMENT_PENDING 4종을 진행 중으로 보며 중복 접수 UNIQUE 대상이다';

COMMIT;


/* ---------------------------------------------------------------------------
 * ROLLBACK (필요 시 수동 실행)
 * ---------------------------------------------------------------------------
BEGIN;

ALTER TABLE "demand" DROP CONSTRAINT "ck_demand_status";
ALTER TABLE "demand" ADD CONSTRAINT "ck_demand_status"
    CHECK ("status" IN (
        'UNASSIGNED','SUBSTITUTE_OFFERED','ASSIGNED','PAYMENT_PENDING',
        'CLOSED','CANCELED','EXPIRED','DELETED'
    ));

COMMENT ON COLUMN "demand"."status" IS 'UNASSIGNED(미배정 — 클러스터 편입 대기, 최대 2일) / SUBSTITUTE_OFFERED(대체상품 제안 — 유사 상품 클러스터 편입 제안에 대한 응답 대기) / ASSIGNED(배정완료 — 수요보드 편입, 진행 중) / PAYMENT_PENDING(낙찰 확정 — 본인 48시간 결제 대기) / CLOSED(종료 — 본인 결제 완료로 요청 종결. 공구 전체 성립 여부와 무관) / CANCELED(사용자 취소 — 참여 취소 MVP 포함 여부 확정 대기) / EXPIRED(소멸 — 미배정 2일 경과, 클러스터 생성 실패, 제안 무응답) / DELETED(운영자·시스템 무효화). UNASSIGNED·SUBSTITUTE_OFFERED·ASSIGNED·PAYMENT_PENDING 4종을 진행 중으로 보며 중복 접수 UNIQUE 대상이다';

COMMIT;
 * ------------------------------------------------------------------------- */
