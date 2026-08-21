package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(example = """
        {
          "password": "password1!",
          "bankName": "카카오뱅크",
          "accountNumber": "3333-12-3456789",
          "depositorName": "홍길동"
        }
        """)
public record AccountEditRequest(
        @NotBlank String password,
        @NotBlank String bankName,
        @NotBlank String accountNumber,
        @NotBlank String depositorName
) {
}
