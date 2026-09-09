package com.moongcheap_backend.product.application.product;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.moongcheap_backend.product.domain.productCatalog.ProductCatalog;
import com.moongcheap_backend.product.infrastructure.productCatalog.ProductCatalogRepository;
import com.moongcheap_backend.product.infrastructure.productSearch.ProductCatalogSearchRepository;
import com.moongcheap_backend.product.infrastructure.productSearch.ProductSearchDocument;
import com.moongcheap_backend.product.presentation.productSearch.dto.ProductSearchResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCatalogSearchRepository productSearchRepository;
    private final ProductCatalogRepository productCatalogRepository;

    public void index(Long catalogId) throws IOException {
        var catalog = productCatalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "카탈로그를 찾을 수 없습니다."));
        productSearchRepository.save(catalog);
    }

    public void indexAll(List<Long> catalogIds) throws IOException {
        List<ProductCatalog> catalogs = productCatalogRepository.findAllById(catalogIds);
        productSearchRepository.saveAll(catalogs);
    }

    public void delete(Long id) throws IOException {
        productSearchRepository.delete(id);
    }

    public ProductSearchResponse search(String keyword, int page, int size) throws IOException {
        int from = page * size;
        List<ProductSearchDocument> docs =
            productSearchRepository.searchByName(keyword, from, size + 1);
        return ProductSearchResponse.of(docs, page, size);
    }
}
