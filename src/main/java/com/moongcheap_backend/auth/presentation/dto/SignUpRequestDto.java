package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(
        @NotBlank String loginId,
        @NotBlank String password,
        @NotBlank String passwordConfirm,
        @NotBlank String nickname,
        @Email String email
) {
}
