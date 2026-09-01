package com.moongcheap_backend.auth.application;

import com.moongcheap_backend.auth.infrastructure.oauth.GoogleOAuth2Client;
import com.moongcheap_backend.auth.infrastructure.oauth.KakaoOAuth2Client;
import com.moongcheap_backend.auth.presentation.dto.WithdrawRequestDto;
import com.moongcheap_backend.auth.infrastructure.port.WithdrawEligibilityChecker;
import com.moongcheap_backend.auth.infrastructure.session.AuthSessionManager;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.LocalCredential;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.member.domain.SocialCredential;
import com.moongcheap_backend.member.domain.SocialProvider;
import com.moongcheap_backend.member.infrastructure.LocalCredentialRepository;
import com.moongcheap_backend.notification.infrastructure.NotificationOptOutRepository;
import com.moongcheap_backend.member.infrastructure.MemberRepository;
import com.moongcheap_backend.member.infrastructure.ShippingAddressRepository;
import com.moongcheap_backend.member.infrastructure.SocialCredentialRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final MemberRepository memberRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final SocialCredentialRepository socialCredentialRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final NotificationOptOutRepository notificationOptOutRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionManager sessionManager;
    private final WithdrawEligibilityChecker eligibilityChecker;
    private final KakaoOAuth2Client kakaoOAuth2Client;
    private final GoogleOAuth2Client googleOAuth2Client;

    @Transactional
    public void withdraw(Long memberId, WithdrawRequestDto request) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Optional<LocalCredential> localCredential = localCredentialRepository.findByMemberId(memberId);

        if (localCredential.isPresent()) {
            verifyLocalWithdraw(request, localCredential.get());
        } else {
            verifySocialWithdraw();
        }

        eligibilityChecker.ensureWithdrawable(memberId);

        List<SocialCredential> socials = socialCredentialRepository.findAllByMemberId(memberId);
        String googleAccessToken = currentGoogleAccessToken();

        sessionManager.invalidateAllForMember(memberId);

        shippingAddressRepository.deleteAllByMemberId(memberId);
        notificationOptOutRepository.deleteAllByMemberId(memberId);
        socialCredentialRepository.deleteByMemberId(memberId);
        localCredentialRepository.deleteByMemberId(memberId);

        member.withdraw();

        unlinkFromProviders(socials, googleAccessToken);
    }

    private void unlinkFromProviders(List<SocialCredential> socials, String googleAccessToken) {
        for (SocialCredential cred : socials) {
            switch (cred.getProvider()) {
                case KAKAO -> kakaoOAuth2Client.unlink(cred.getProviderId());
                case GOOGLE -> googleOAuth2Client.revoke(googleAccessToken);
            }
        }
    }

    private String currentGoogleAccessToken() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs)) return null;
        HttpSession session = attrs.getRequest().getSession(false);
        if (session == null) return null;
        return (String) session.getAttribute(AuthSessionManager.GOOGLE_ACCESS_TOKEN_ATTR);
    }

    private void verifyLocalWithdraw(WithdrawRequestDto request, LocalCredential credential) {
        if (request == null || request.password() == null || request.password().isBlank()) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }
        if (!passwordEncoder.matches(request.password(), credential.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
    }

    private void verifySocialWithdraw() {
        // TODO: 이메일 인증 추가 예정
    }
}
