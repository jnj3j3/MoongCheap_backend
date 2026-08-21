package com.moongcheap_backend.auth.application;

import com.moongcheap_backend.common.security.MemberRole;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.domain.Member;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PrincipalFactory {

    public SessionPrincipal build(Member member) {
        Set<MemberRole> roles = new HashSet<>();
        roles.add(MemberRole.BUYER);
        if (member.isSeller()) {
            roles.add(MemberRole.SELLER);
        }
        return new SessionPrincipal(
                member.getId(),
                member.getLoginId(),
                member.getNickname(),
                roles,
                member.isSeller()
        );
    }
}
