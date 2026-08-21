package com.moongcheap_backend.auth.application;

import com.moongcheap_backend.auth.presentation.dto.LoginRequestDto;
import com.moongcheap_backend.auth.infrastructure.session.AuthSessionManager;
import com.moongcheap_backend.auth.infrastructure.session.LoginFailureCounter;
import com.moongcheap_backend.auth.domain.LoginIdValidator;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.domain.LocalCredential;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.member.infrastructure.LocalCredentialRepository;
import com.moongcheap_backend.member.infrastructure.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final MemberRepository memberRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginFailureCounter failureCounter;
    private final AuthSessionManager sessionManager;
    private final PrincipalFactory principalFactory;

    // 동일 계정 로그인 시도 가능 횟수 5회, 초과시 10분 잠금
    // login 유지의 경우 기본 24시간, 로그인 유지 설정의 경우 14일
    @Transactional
    public SessionPrincipal login(LoginRequestDto request, HttpServletRequest httpRequest) {
        String loginId = LoginIdValidator.normalizeAndValidate(request.loginId());
        /* 현재 동일 login id, password 방어로직만 존재하며, 이에 대한 브루트 포스 및 디도스 공격에 대한 방어 로직은 존재하지 않음
        * 이를 위해 ip lock을 걸려했지만, 이는 nginx proxy가 붙을지 모르기 때문에 일단 추가하지 않음.
        */
        if (failureCounter.isLocked(loginId)) {
            throw new BusinessException(ErrorCode.LOGIN_LOCKED);
        }
        Member member = memberRepository.findByLoginIdAndDeletedAtIsNull(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));
        LocalCredential credential = localCredentialRepository.findByMemberId(member.getId())
                .orElseThrow(() -> {
                    failureCounter.recordFailure(loginId);
                    return new BusinessException(ErrorCode.LOGIN_FAILED);
                });
        if (!passwordEncoder.matches(request.password(), credential.getPassword())) {
            failureCounter.recordFailure(loginId);
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        failureCounter.reset(loginId);
        member.updateLastLoginAt();
        SessionPrincipal principal = principalFactory.build(member);
        sessionManager.bindPrincipal(httpRequest, principal, request.rememberMe());
        return principal;
    }

    public void logout(HttpServletRequest request) {
        sessionManager.invalidateCurrent(request);
    }
}
