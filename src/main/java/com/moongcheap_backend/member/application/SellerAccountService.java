package com.moongcheap_backend.member.application;

import com.moongcheap_backend.auth.infrastructure.port.SettlementInProgressChecker;
import com.moongcheap_backend.common.crypto.EncryptionService;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.LocalCredential;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.member.infrastructure.LocalCredentialRepository;
import com.moongcheap_backend.member.infrastructure.SellerRepository;
import com.moongcheap_backend.member.presentation.dto.AccountEditRequest;
import com.moongcheap_backend.member.presentation.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerAccountService {

    private final SellerRepository sellerRepository;
    private final LocalCredentialRepository localCredentialRepository;
    private final EncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;
    private final SettlementInProgressChecker settlementChecker;

    @Transactional(readOnly = true)
    public AccountResponse detail(Long memberId) {
        Seller seller = getSeller(memberId);
        String accountPlain = encryptionService.decrypt(seller.getBankAccount());
        return new AccountResponse(
                seller.getBankName(),
                encryptionService.maskAccountNumber(accountPlain),
                seller.getDepositorName()
        );
    }

    @Transactional
    public void edit(Long memberId, AccountEditRequest request) {
        LocalCredential credential = localCredentialRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOCAL_CREDENTIAL_REQUIRED));
        if (!passwordEncoder.matches(request.password(), credential.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        Seller seller = getSeller(memberId);
        settlementChecker.ensureAccountEditable(seller.getId());

        String digits = request.accountNumber().replaceAll("[^0-9]", "");
        seller.updateMutable(null, request.bankName(), encryptionService.encrypt(digits), request.depositorName());
    }

    private Seller getSeller(Long memberId) {
        return sellerRepository.findByMemberIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SELLER_NOT_FOUND));
    }
}
