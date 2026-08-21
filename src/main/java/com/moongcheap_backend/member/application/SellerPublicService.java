package com.moongcheap_backend.member.application;

import com.moongcheap_backend.common.crypto.EncryptionService;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.member.infrastructure.SellerRepository;
import com.moongcheap_backend.member.presentation.dto.SellerPublicResponse;
import com.moongcheap_backend.member.presentation.dto.SettlementAccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerPublicService {

    private final SellerRepository sellerRepository;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public SellerPublicResponse detail(Long sellerId) {
        Seller seller = sellerRepository.findByIdAndDeletedAtIsNull(sellerId)
                .filter(Seller::isSellable)
                .orElseThrow(() -> new BusinessException(ErrorCode.SELLER_NOT_FOUND));
        String bizPlain = encryptionService.decrypt(seller.getBusinessNumber());
        return new SellerPublicResponse(
                seller.getBusinessName(),
                seller.getOwnerName(),
                encryptionService.maskBusinessNumber(bizPlain),
                seller.getMailOrderRegistrationNumber(),
                seller.getPhoneNumber()
        );
    }

    @Transactional(readOnly = true)
    public SettlementAccountInfo getForSettlement(Long sellerId) {
        Seller seller = sellerRepository.findByIdAndDeletedAtIsNull(sellerId)
                .filter(Seller::isSellable)
                .orElseThrow(() -> new BusinessException(ErrorCode.SELLER_NOT_APPROVED));
        return new SettlementAccountInfo(
                seller.getId(),
                seller.getBankName(),
                encryptionService.decrypt(seller.getBankAccount()),
                seller.getDepositorName(),
                encryptionService.decrypt(seller.getBusinessNumber()),
                seller.getBusinessName()
        );
    }

}
