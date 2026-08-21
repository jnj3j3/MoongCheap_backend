package com.moongcheap_backend.auth.application;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.SocialCredential;
import com.moongcheap_backend.member.domain.SocialProvider;
import com.moongcheap_backend.member.infrastructure.LocalCredentialRepository;
import com.moongcheap_backend.member.infrastructure.SocialCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialLinkService {

    private final SocialCredentialRepository socialCredentialRepository;
    private final LocalCredentialRepository localCredentialRepository;

    @Transactional
    public void link(Long memberId, SocialProvider provider, String providerId) {
        socialCredentialRepository.findByProviderAndProviderId(provider, providerId)
                .ifPresent(existing -> {
                    if (!existing.getMemberId().equals(memberId)) {
                        throw new BusinessException(ErrorCode.SOCIAL_ALREADY_LINKED);
                    }
                });
        if (socialCredentialRepository.findByMemberIdAndProvider(memberId, provider).isPresent()) {
            return;
        }
        socialCredentialRepository.save(SocialCredential.builder()
                .memberId(memberId)
                .provider(provider)
                .providerId(providerId)
                .build());
    }

    @Transactional
    public void unlink(Long memberId, SocialProvider provider) {
        SocialCredential target = socialCredentialRepository.findByMemberIdAndProvider(memberId, provider)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        long socialCount = socialCredentialRepository.countByMemberId(memberId);
        boolean hasLocal = localCredentialRepository.existsByMemberId(memberId);
        int remainingCredentials = (int) socialCount - 1 + (hasLocal ? 1 : 0);
        if (remainingCredentials < 1) {
            throw new BusinessException(ErrorCode.LAST_CREDENTIAL_CANNOT_UNLINK);
        }
        socialCredentialRepository.delete(target);
    }
}
