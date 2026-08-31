package com.moongcheap_backend.common.response;

public record IdResponse(Long id) {
    public static IdResponse of(Long id) {
        return new IdResponse(id);
    }
}
