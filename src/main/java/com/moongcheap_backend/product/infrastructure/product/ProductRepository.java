package com.moongcheap_backend.product.infrastructure.product;

import com.moongcheap_backend.product.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
