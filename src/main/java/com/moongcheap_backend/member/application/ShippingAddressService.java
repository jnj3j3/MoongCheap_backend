package com.moongcheap_backend.member.application;

import com.moongcheap_backend.common.crypto.EncryptionService;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.ShippingAddress;
import com.moongcheap_backend.member.infrastructure.ShippingAddressRepository;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressEditRequest;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressRequest;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private static final int MAX_ADDRESSES = 5;

    private final ShippingAddressRepository shippingAddressRepository;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public List<ShippingAddressResponse> getAll(Long memberId) {
        return shippingAddressRepository
                .findAllByMemberIdOrderByIsDefaultDescCreatedAtDesc(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShippingAddressResponse getById(Long memberId, Long addressId) {
        return toResponse(loadOwned(memberId, addressId));
    }

    // 배송지 저장 제한 : 5개
    @Transactional
    public Long create(Long memberId, ShippingAddressRequest request) {
        long existing = shippingAddressRepository.countByMemberId(memberId);
        if (existing >= MAX_ADDRESSES) {
            throw new BusinessException(ErrorCode.SHIPPING_ADDRESS_LIMIT_EXCEEDED);
        }
        boolean shouldBeDefault = existing == 0 || request.setAsDefault();
        if (shouldBeDefault) {
            shippingAddressRepository.findByMemberIdAndIsDefaultTrue(memberId)
                    .ifPresent(ShippingAddress::unmarkDefault);
            shippingAddressRepository.flush();
        }
        String phoneDigits = request.phoneNumber().replaceAll("[^0-9]", "");
        ShippingAddress saved = shippingAddressRepository.save(ShippingAddress.builder()
                .memberId(memberId)
                .alias(request.alias())
                .recipientName(request.recipientName())
                .phoneNumber(encryptionService.encrypt(phoneDigits))
                .zipcode(request.zipcode())
                .address(request.address())
                .addressDetail(request.addressDetail())
                .requestMessage(request.requestMessage())
                .isDefault(shouldBeDefault)
                .build());
        return saved.getId();
    }

    @Transactional
    public void edit(Long memberId, Long addressId, ShippingAddressEditRequest request) {
        ShippingAddress address = loadOwned(memberId, addressId);
        String phoneDigits = request.phoneNumber().replaceAll("[^0-9]", "");
        address.update(
                request.alias(),
                request.recipientName(),
                encryptionService.encrypt(phoneDigits),
                request.zipcode(),
                request.address(),
                request.addressDetail(),
                request.requestMessage()
        );
    }

    @Transactional
    public void delete(Long memberId, Long addressId) {
        int deleted = shippingAddressRepository.deleteByIdAndMemberId(addressId, memberId);
        if (deleted == 0) {
            throw new BusinessException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND);
        }
    }

    @Transactional
    public void markAsDefault(Long memberId, Long addressId) {
        ShippingAddress target = loadOwned(memberId, addressId);
        if (target.isDefault()) return;
        shippingAddressRepository.findByMemberIdAndIsDefaultTrue(memberId)
                .ifPresent(ShippingAddress::unmarkDefault);
        shippingAddressRepository.flush();
        target.markAsDefault();
    }

    private ShippingAddress loadOwned(Long memberId, Long addressId) {
        ShippingAddress address = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHIPPING_ADDRESS_NOT_FOUND));
        if (!address.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.SHIPPING_ADDRESS_FORBIDDEN);
        }
        return address;
    }

    private ShippingAddressResponse toResponse(ShippingAddress a) {
        String phone = encryptionService.decrypt(a.getPhoneNumber());
        return new ShippingAddressResponse(
                a.getId(),
                a.getAlias(),
                a.getRecipientName(),
                encryptionService.maskPhoneNumber(phone),
                a.getZipcode(),
                a.getAddress(),
                a.getAddressDetail(),
                a.getRequestMessage(),
                a.isDefault()
        );
    }
}
