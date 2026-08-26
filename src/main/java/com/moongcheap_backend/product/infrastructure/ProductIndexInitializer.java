package com.moongcheap_backend.product.infrastructure;

import jakarta.json.stream.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.json.JsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "opensearch.auto-create-indices", havingValue = "true")
public class ProductIndexInitializer implements CommandLineRunner {

    private static final String INDEX_NAME = "product_v1";
    private static final String ALIAS_NAME = "product";
    private static final String SETTINGS_RESOURCE = "opensearch/product-index.json";

    private final OpenSearchClient openSearchClient;

    @Override
    public void run(String... args) throws Exception {
        boolean indexExists = openSearchClient.indices()
                .exists(e -> e.index(INDEX_NAME))
                .value();
        if (indexExists) {
            log.info("OpenSearch index '{}' already exists, skipping creation.", INDEX_NAME);
            ensureAlias();
            return;
        }

        CreateIndexRequest body = loadIndexDefinition();
        openSearchClient.indices().create(c -> c
                .index(INDEX_NAME)
                .settings(body.settings())
                .mappings(body.mappings())
                .aliases(ALIAS_NAME, a -> a)
        );
        log.info("Created OpenSearch index '{}' with alias '{}'.", INDEX_NAME, ALIAS_NAME);
    }

    private CreateIndexRequest loadIndexDefinition() throws Exception {
        JsonpMapper mapper = openSearchClient._transport().jsonpMapper();
        try (InputStream in = new ClassPathResource(SETTINGS_RESOURCE).getInputStream();
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             JsonParser parser = mapper.jsonProvider().createParser(reader)) {
            return CreateIndexRequest._DESERIALIZER.deserialize(parser, mapper);
        }
    }

    private void ensureAlias() throws Exception {
        boolean aliasExists = openSearchClient.indices()
                .existsAlias(e -> e.name(ALIAS_NAME))
                .value();
        if (aliasExists) {
            return;
        }
        openSearchClient.indices().putAlias(p -> p.index(INDEX_NAME).name(ALIAS_NAME));
        log.info("Attached alias '{}' to existing index '{}'.", ALIAS_NAME, INDEX_NAME);
    }
}
