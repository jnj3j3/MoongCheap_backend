package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.Size;

public record WithdrawRequestDto(
        @Size(max = 100) String password
) {
}
