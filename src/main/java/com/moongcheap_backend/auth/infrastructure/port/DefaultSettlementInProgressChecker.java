package com.moongcheap_backend.auth.infrastructure.port;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(name = "settlementDomainInProgressChecker")
public class DefaultSettlementInProgressChecker implements SettlementInProgressChecker {

    @Override
    public void ensureAccountEditable(Long sellerId) {
        // 정산 도메인이 아직 없으므로 통과.
    }
}
