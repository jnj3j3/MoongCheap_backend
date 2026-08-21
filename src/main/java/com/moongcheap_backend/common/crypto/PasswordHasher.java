package com.moongcheap_backend.common.crypto;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.context.annotation.Bean;

@Configuration
public class PasswordHasher {

    @Bean
    public PasswordEncoder passwordEncoder() {
        int saltLength = 16;
        int hashLength = 32;
        int parallelism = 1;
        int memory = 19456;
        int iterations = 2;
        return new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
    }
}
