package com.moongcheap_backend.auth.presentation.dto;

public record LoginIdAvailabilityResponseDto(
        String loginId,
        boolean available
) {
}

