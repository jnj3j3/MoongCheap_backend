package com.moongcheap_backend.auth.infrastructure.port;

public interface SettlementInProgressChecker {

    /**
     * 정산 처리 중인 건이 있으면 예외를 던진다.
     * 정산 도메인이 없으면 기본 no-op 구현이 사용된다.
     */
    void ensureAccountEditable(Long sellerId);
}
