package com.moongcheap_backend.common.security;

import java.io.Serializable;
import java.util.Set;

public record SessionPrincipal(
        Long memberId,
        String loginId,
        String nickname,
        Set<MemberRole> roles,
        boolean sellerApproved,
        boolean termsAgreed
) implements Serializable {
}
