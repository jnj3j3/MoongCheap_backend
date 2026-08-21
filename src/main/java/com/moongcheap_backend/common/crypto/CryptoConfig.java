package com.moongcheap_backend.common.crypto;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoConfig {
    // AES-256 CBC + PKCS5Padding
    @Bean
    public TextEncryptor textEncryptor(CryptoProperties properties) {
        return Encryptors.delux(properties.password(), properties.salt());
    }
}
