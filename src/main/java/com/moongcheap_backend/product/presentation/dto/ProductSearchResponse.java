package com.moongcheap_backend.product.presentation.dto;

import com.moongcheap_backend.product.domain.Product;

public record ProductSearchResponse(
        Long id,
        String name
) {
    public static ProductSearchResponse from(Product product) {
        return new ProductSearchResponse(product.getId(), product.getName());
    }
}
