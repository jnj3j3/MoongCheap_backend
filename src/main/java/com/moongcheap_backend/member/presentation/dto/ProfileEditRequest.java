package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(example = """
        {
          "nickname": "홍길동",
          "phoneNumber": "010-1234-5678",
          "email": "hong@example.com"
        }
        """)
public record ProfileEditRequest(
        String nickname,
        String phoneNumber,
        @Email String email
) {
}
