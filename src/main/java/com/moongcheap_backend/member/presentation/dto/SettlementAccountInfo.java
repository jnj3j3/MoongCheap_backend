package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "sellerId": 1,
          "bankName": "카카오뱅크",
          "accountNumber": "3333-12-3456789",
          "depositorName": "홍길동",
          "businessNumber": "1234567891",
          "businessName": "문치프 스토어"
        }
        """)
public record SettlementAccountInfo(
        Long sellerId,
        String bankName,
        String accountNumber,
        String depositorName,
        String businessNumber,
        String businessName
) {
}
