package com.moongcheap_backend.common.security;

public record CurrentMember(
        Long memberId,
        String loginId,
        String nickname
) {
}
