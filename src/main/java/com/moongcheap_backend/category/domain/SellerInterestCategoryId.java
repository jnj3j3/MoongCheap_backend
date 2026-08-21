package com.moongcheap_backend.category.domain;

import java.io.Serializable;
import java.util.Objects;

public class SellerInterestCategoryId implements Serializable {
    private Long sellerId;
    private Long categoryId;

    public SellerInterestCategoryId() {}
    public SellerInterestCategoryId(Long sellerId, Long categoryId) {
        this.sellerId = sellerId;
        this.categoryId = categoryId;
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerInterestCategoryId that)) return false;
        return Objects.equals(sellerId, that.sellerId) && Objects.equals(categoryId, that.categoryId);
    }
    @Override public int hashCode() { return Objects.hash(sellerId, categoryId); }
}
