package com.moongcheap_backend.common.crypto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "moongcheap.security.encryption")
public record CryptoProperties(
        String password,
        String salt
) {
}
