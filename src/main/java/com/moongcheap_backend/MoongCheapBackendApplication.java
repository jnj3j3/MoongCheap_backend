package com.moongcheap_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoongCheapBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoongCheapBackendApplication.class, args);
    }

}
