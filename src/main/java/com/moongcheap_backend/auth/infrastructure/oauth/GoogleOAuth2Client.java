package com.moongcheap_backend.auth.infrastructure.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth2Client {

    private static final String REVOKE_URL = "https://oauth2.googleapis.com/revoke";

    private final RestClient restClient;

    public void revoke(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Google token not available; skipping revoke");
            return;
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        try {
            restClient.post()
                    .uri(REVOKE_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Google revoke success");
        } catch (Exception e) {
            log.warn("Google revoke failed", e);
        }
    }
}
