package com.moongcheap_backend.product.infrastructure.productCatalog;

import com.moongcheap_backend.product.domain.productCatalog.ProductCatalog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCatalogRespository extends JpaRepository<ProductCatalog, Long> {

    List<ProductCatalog> findAllByOrderByIdDesc(Pageable pageable);
}
