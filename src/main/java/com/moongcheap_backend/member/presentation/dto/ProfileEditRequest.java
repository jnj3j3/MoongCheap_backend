package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(example = """
        {
          "nickname": "홍길동",
          "phoneNumber": "010-1234-5678",
          "email": "hong@example.com"
        }
        """)
public record ProfileEditRequest(
        @Size(max = 20) String nickname,
        @Pattern(regexp = "^0\\d{1,2}-?\\d{3,4}-?\\d{4}$") String phoneNumber,
        @Email String email
) {
}
