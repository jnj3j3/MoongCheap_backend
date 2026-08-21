package com.moongcheap_backend.auth.infrastructure.port;

public interface WithdrawEligibilityChecker {

    /**
     * 진행 중 주문/구매신청/공구가 있으면 예외를 던진다.
     * 주문 도메인이 구현체를 제공하기 전까지는 기본 no-op 구현이 사용된다.
     */
    void ensureWithdrawable(Long memberId);
}
