package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "bankName": "카카오뱅크",
          "accountNumberMasked": "********6789",
          "depositorName": "홍길동"
        }
        """)
public record AccountResponse(
        String bankName,
        String accountNumberMasked,
        String depositorName
) {
}
