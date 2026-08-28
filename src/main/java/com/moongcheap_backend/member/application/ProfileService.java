package com.moongcheap_backend.member.application;

import com.moongcheap_backend.auth.application.NicknameService;
import com.moongcheap_backend.auth.domain.NicknameValidator;
import com.moongcheap_backend.common.crypto.EncryptionService;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.member.domain.SocialCredential;
import com.moongcheap_backend.member.infrastructure.MemberRepository;
import com.moongcheap_backend.member.infrastructure.SellerRepository;
import com.moongcheap_backend.member.infrastructure.SocialCredentialRepository;
import com.moongcheap_backend.member.presentation.dto.ProfileEditRequestDto;
import com.moongcheap_backend.member.presentation.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final SocialCredentialRepository socialCredentialRepository;
    private final NicknameService nicknameService;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public ProfileResponseDto detail(Long memberId) {
        Member member = getMember(memberId);
        List<SocialCredential> socials = socialCredentialRepository.findAllByMemberId(memberId);
        ProfileResponseDto.SellerSummary sellerSummary = member.isSeller()
                ? sellerRepository.findByMemberIdAndDeletedAtIsNull(memberId)
                        .map(this::toSellerSummary)
                        .orElseThrow(() -> new IllegalStateException("is_seller=true인데 seller 레코드가 없습니다: " + memberId))
                : null;
        String maskedPhone = member.getPhoneNumber() == null ? null :
                encryptionService.maskPhoneNumber(encryptionService.decrypt(member.getPhoneNumber()));
        return new ProfileResponseDto(
                member.getLoginId(),
                member.getNickname(),
                maskedPhone,
                member.getEmail(),
                member.getCreatedAt(),
                member.isSeller(),
                socials.stream().map(SocialCredential::getProvider).toList(),
                sellerSummary
        );
    }

    @Transactional
    public void edit(Long memberId, ProfileEditRequestDto request) {
        Member member = getMember(memberId);
        String nickname = null;
        if (request.nickname() != null && !request.nickname().isBlank()) {
            String candidate = NicknameValidator.toKey(NicknameValidator.normalize(request.nickname()));
            if (!candidate.equalsIgnoreCase(member.getNickname())) {
                nicknameService.ensureAvailable(candidate);
            }
            nickname = candidate;
        }
        String phoneEncrypted = null;
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            String digits = request.phoneNumber().replaceAll("[^0-9]", "");
            phoneEncrypted = encryptionService.encrypt(digits);
        }
        member.changeProfile(nickname, request.email(), phoneEncrypted);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private ProfileResponseDto.SellerSummary toSellerSummary(Seller seller) {
        return new ProfileResponseDto.SellerSummary(
                seller.getBusinessName(),
                seller.getStatus().name()
        );
    }
}
