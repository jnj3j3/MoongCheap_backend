package com.moongcheap_backend.common.response;

import java.util.List;

public record ApiError(
        String code,
        String message,
        List<FieldError> fieldErrors
) {
    public static ApiError of(String code, String message) {
        return new ApiError(code, message, List.of());
    }

    public static ApiError of(String code, String message, List<FieldError> fieldErrors) {
        return new ApiError(code, message, fieldErrors);
    }

    public record FieldError(String field, String message) {}
}
