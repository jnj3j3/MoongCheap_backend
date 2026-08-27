package com.moongcheap_backend.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(example = """
        {
          "businessName": "문치프 스토어",
          "businessNumber": "123-45-67891",
          "mailOrderRegistrationNumber": "2024-서울강남-1234",
          "ownerName": "홍길동",
          "phoneNumber": "010-1234-5678",
          "interestCategoryIds": [1, 2, 3]
        }
        """)
public record SellerRegisterRequestDto(
        @NotBlank @Size(max = 50) String businessName,
        @NotBlank String businessNumber,
        @NotBlank @Size(max = 30) String mailOrderRegistrationNumber,
        @NotBlank @Size(max = 50) String ownerName,
        @NotBlank @Size(max = 20) String phoneNumber,
        @NotEmpty List<Long> interestCategoryIds
) {
}
