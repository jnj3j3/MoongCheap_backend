package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank @Size(max = 100) String currentPassword,
        @NotBlank @Size(max = 100) String newPassword,
        @NotBlank @Size(max = 100) String newPasswordConfirm
) {
}
