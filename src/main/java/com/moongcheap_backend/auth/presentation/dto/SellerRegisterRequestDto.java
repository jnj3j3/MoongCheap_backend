package com.moongcheap_backend.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(example = """
        {
          "businessName": "문치프 스토어",
          "businessNumber": "123-45-67891",
          "mailOrderRegistrationNumber": "2024-서울강남-1234",
          "ownerName": "홍길동",
          "phoneNumber": "010-1234-5678"
        }
        """)
public record SellerRegisterRequestDto(
        @NotBlank @Size(max = 50) String businessName,
        @NotBlank String businessNumber,
        @NotBlank @Size(max = 30) String mailOrderRegistrationNumber,
        @NotBlank @Size(max = 50) String ownerName,
        @NotBlank @Pattern(regexp = "^0\\d{1,2}-?\\d{3,4}-?\\d{4}$") @Size(max = 20) String phoneNumber
) {
}
