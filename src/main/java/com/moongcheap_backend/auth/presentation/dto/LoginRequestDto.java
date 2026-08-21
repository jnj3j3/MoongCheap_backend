package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String loginId,
        @NotBlank String password,
        boolean rememberMe
) {
}
