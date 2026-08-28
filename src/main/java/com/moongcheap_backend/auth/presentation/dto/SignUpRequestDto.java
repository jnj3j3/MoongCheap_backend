package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDto(
        @NotBlank @Size(max = 50) String loginId,
        @NotBlank @Size(max = 100) String password,
        @NotBlank @Size(max = 100) String passwordConfirm,
        @NotBlank @Size(max = 20) String nickname,
        @Email String email
) {
}
