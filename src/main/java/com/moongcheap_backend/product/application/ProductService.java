package com.moongcheap_backend.product.application;

import com.moongcheap_backend.product.domain.Product;
import com.moongcheap_backend.product.infrastructure.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductSearchRepository productSearchRepository;

    public void index(Product product) throws IOException {
        productSearchRepository.save(product);
    }

    public void delete(Long id) throws IOException {
        productSearchRepository.delete(id);
    }

    public List<Product> search(String keyword, int size) throws IOException {
        return productSearchRepository.searchByName(keyword, size);
    }
}
