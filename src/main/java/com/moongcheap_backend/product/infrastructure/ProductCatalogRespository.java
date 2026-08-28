package com.moongcheap_backend.product.infrastructure;

import com.moongcheap_backend.product.domain.ProductCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCatalogRespository extends JpaRepository<ProductCatalog, Long> {

}
