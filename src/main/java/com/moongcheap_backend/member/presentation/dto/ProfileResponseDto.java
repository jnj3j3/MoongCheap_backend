package com.moongcheap_backend.member.presentation.dto;

import com.moongcheap_backend.member.domain.SocialProvider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(example = """
        {
          "loginId": "hong123",
          "nickname": "홍길동",
          "phoneNumberMasked": "010-****-5678",
          "email": "hong@example.com",
          "joinedAt": "2024-01-15T10:30:00",
          "isSeller": true,
          "linkedProviders": ["KAKAO"],
          "seller": {
            "businessName": "문치프 스토어",
            "status": "APPROVED"
          }
        }
        """)
public record ProfileResponseDto(
        String loginId,
        String nickname,
        String phoneNumberMasked,
        String email,
        LocalDateTime joinedAt,
        boolean isSeller,
        List<SocialProvider> linkedProviders,
        SellerSummary seller
) {
    public record SellerSummary(
            String businessName,
            String status
    ) {}
}
