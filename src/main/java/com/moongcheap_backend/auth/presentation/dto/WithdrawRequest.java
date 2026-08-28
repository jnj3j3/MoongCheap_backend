package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.Size;

public record WithdrawRequest(
        @Size(max = 100) String password
) {
}
