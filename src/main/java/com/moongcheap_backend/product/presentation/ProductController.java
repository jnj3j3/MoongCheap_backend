package com.moongcheap_backend.product.presentation;

import com.moongcheap_backend.product.application.ProductService;
import com.moongcheap_backend.product.presentation.dto.ProductIndexRequest;
import com.moongcheap_backend.product.presentation.dto.ProductSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Tag(name = "Product", description = "상품 검색/색인")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 색인", description = "OpenSearch 'product' 별칭에 상품 문서를 upsert.")
    @PostMapping
    public ResponseEntity<Void> index(@Valid @RequestBody ProductIndexRequest request) throws IOException {
        productService.index(request.toDomain());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 색인 삭제", description = "OpenSearch에서 상품 문서를 id로 삭제.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws IOException {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 검색", description = "상품명 검색. name(standard)과 name.ngram(2-gram/3-gram)에 boost 차등 적용.")
    @GetMapping("/search")
    public ResponseEntity<List<ProductSearchResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int size
    ) throws IOException {
        List<ProductSearchResponse> results = productService.search(q, size).stream()
                .map(ProductSearchResponse::from)
                .toList();
        return ResponseEntity.ok(results);
    }
}
