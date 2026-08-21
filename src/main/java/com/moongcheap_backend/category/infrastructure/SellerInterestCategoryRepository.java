package com.moongcheap_backend.category.infrastructure;

import com.moongcheap_backend.category.domain.SellerInterestCategory;
import com.moongcheap_backend.category.domain.SellerInterestCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellerInterestCategoryRepository extends JpaRepository<SellerInterestCategory, SellerInterestCategoryId> {
    List<SellerInterestCategory> findAllBySellerId(Long sellerId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SellerInterestCategory c WHERE c.sellerId = :sellerId")
    void deleteAllBySellerId(@Param("sellerId") Long sellerId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SellerInterestCategory c WHERE c.sellerId = :sellerId AND c.categoryId = :categoryId")
    void deleteBySellerIdAndCategoryId(@Param("sellerId") Long sellerId, @Param("categoryId") Long categoryId);
}
