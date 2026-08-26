package com.moongcheap_backend.product.infrastructure;

import com.moongcheap_backend.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.Refresh;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {

    private static final String ALIAS = "product";

    private final OpenSearchClient openSearchClient;

    public void save(Product product) throws IOException {
        openSearchClient.index(i -> i
                .index(ALIAS)
                .id(String.valueOf(product.getId()))
                .document(product)
                .refresh(Refresh.WaitFor)
        );
    }

    public void delete(Long id) throws IOException {
        openSearchClient.delete(d -> d
                .index(ALIAS)
                .id(String.valueOf(id))
                .refresh(Refresh.WaitFor)
        );
    }

    public List<Product> searchByName(String keyword, int size) throws IOException {
        Query query = Query.of(q -> q
                .multiMatch(m -> m
                        .query(keyword)
                        .fields("name^3", "name.ngram^1")
                )
        );
        SearchResponse<Product> response = openSearchClient.search(s -> s
                        .index(ALIAS)
                        .query(query)
                        .size(size),
                Product.class);
        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .filter(Objects::nonNull)
                .toList();
    }
}
