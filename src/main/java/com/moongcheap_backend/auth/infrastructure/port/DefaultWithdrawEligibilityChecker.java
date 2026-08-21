package com.moongcheap_backend.auth.infrastructure.port;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name = "orderWithdrawEligibilityChecker")
public class DefaultWithdrawEligibilityChecker implements WithdrawEligibilityChecker {

    @Override
    public void ensureWithdrawable(Long memberId) {
        // 주문 도메인이 아직 없으므로 기본 통과. 이후 별도 빈으로 대체.
        // todo: 주문 도메인에서 memberId로 모든 주문 활성화된 주문을 가져와 있을 시 throw error
    }
}
