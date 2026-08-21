package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDto(
        @NotBlank String currentPassword,
        @NotBlank String newPassword,
        @NotBlank String newPasswordConfirm
) {
}
