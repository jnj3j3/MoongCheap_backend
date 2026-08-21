package com.moongcheap_backend.category.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@IdClass(SellerInterestCategoryId.class)
@Table(name = "seller_interest_category")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerInterestCategory {

    public static final int MAX_COUNT = 10;

    @Id
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Id
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private SellerInterestCategory(Long sellerId, Long categoryId) {
        this.sellerId = sellerId;
        this.categoryId = categoryId;
    }
}
