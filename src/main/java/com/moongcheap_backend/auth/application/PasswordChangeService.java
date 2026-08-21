package com.moongcheap_backend.auth.application;

import com.moongcheap_backend.auth.presentation.dto.ChangePasswordRequestDto;
import com.moongcheap_backend.auth.infrastructure.session.AuthSessionManager;
import com.moongcheap_backend.auth.domain.PasswordValidator;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
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
public class PasswordChangeService {

    private final MemberRepository memberRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthSessionManager sessionManager;

    @Transactional
    public void changePassword(Long memberId, ChangePasswordRequestDto request, HttpServletRequest httpRequest) {
        if (!request.newPassword().equals(request.newPasswordConfirm())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        LocalCredential credential = localCredentialRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOCAL_CREDENTIAL_REQUIRED));
        if (!passwordEncoder.matches(request.currentPassword(), credential.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        PasswordValidator.validate(request.newPassword(), member.getLoginId());
        if (passwordEncoder.matches(request.newPassword(), credential.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_SAME_AS_PREVIOUS);
        }
        credential.changePassword(passwordEncoder.encode(request.newPassword()));
        sessionManager.invalidateAllExceptCurrent(memberId, httpRequest);
    }
}
