package com.moongcheap_backend.product.infrastructure.productCatalog;

import com.moongcheap_backend.product.domain.productCatalog.ProductCatalog;
import com.moongcheap_backend.product.domain.productCatalog.ProductCatalogStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCatalogRespository extends JpaRepository<ProductCatalog, Long> {

    List<ProductCatalog> findAllByOrderByIdDesc(Pageable pageable);

    boolean existsByIdAndStatus(Long id, ProductCatalogStatus status);
}
