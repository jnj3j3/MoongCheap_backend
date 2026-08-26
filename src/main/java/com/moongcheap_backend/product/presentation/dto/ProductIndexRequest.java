package com.moongcheap_backend.product.presentation.dto;

import com.moongcheap_backend.product.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductIndexRequest(
        @NotNull Long id,
        @NotBlank String name
) {
    public Product toDomain() {
        return Product.builder().id(id).name(name).build();
    }
}
