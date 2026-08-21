package com.moongcheap_backend.category.application;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.category.domain.SellerInterestCategory;
import com.moongcheap_backend.category.infrastructure.SellerInterestCategoryRepository;
import com.moongcheap_backend.member.infrastructure.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerInterestCategoryService {


    private final SellerRepository sellerRepository;
    private final SellerInterestCategoryRepository interestCategoryRepository;

    @Transactional(readOnly = true)
    public List<Long> getAll(Long memberId) {
        Seller seller = getSeller(memberId);
        return interestCategoryRepository.findAllBySellerId(seller.getId()).stream()
                .map(SellerInterestCategory::getCategoryId)
                .toList();
    }

    @Transactional
    public void update(Long memberId, List<Long> categoryIds) {
        Seller seller = getSeller(memberId);
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BusinessException(ErrorCode.SELLER_INTEREST_CATEGORY_REQUIRED);
        }
        if (categoryIds.size() > SellerInterestCategory.MAX_COUNT) {
            throw new BusinessException(ErrorCode.SELLER_INTEREST_CATEGORY_LIMIT);
        }
        interestCategoryRepository.deleteAllBySellerId(seller.getId());
        List<SellerInterestCategory> toAdd = categoryIds.stream()
                .map(id -> SellerInterestCategory.builder()
                        .sellerId(seller.getId())
                        .categoryId(id)
                        .build())
                .toList();
        interestCategoryRepository.saveAll(toAdd);
    }

    private Seller getSeller(Long memberId) {
        return sellerRepository.findByMemberIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SELLER_NOT_FOUND));
    }
}
