package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "businessName": "문치프 스토어",
          "ownerName": "홍길동",
          "businessNumberMasked": "123-45-67***",
          "mailOrderRegistrationNumber": "2024-서울강남-1234",
          "phoneNumber": "010-****-5678"
        }
        """)
public record SellerPublicResponseDto(
        String businessName,
        String ownerName,
        String businessNumberMasked,
        String mailOrderRegistrationNumber,
        String phoneNumber
) {
}
