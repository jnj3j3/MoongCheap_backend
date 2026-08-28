package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank @Size(max = 50) String loginId,
        @NotBlank @Size(max = 100) String password,
        boolean rememberMe
) {
}
