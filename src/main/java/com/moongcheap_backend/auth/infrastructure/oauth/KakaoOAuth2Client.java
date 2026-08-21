package com.moongcheap_backend.auth.infrastructure.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoOAuth2Client {

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    private final RestClient restClient;
    private final String adminKey;

    public KakaoOAuth2Client(RestClient restClient,
                             @Value("${moongcheap.oauth2.kakao.admin-key:}") String adminKey) {
        this.restClient = restClient;
        this.adminKey = adminKey;
    }

    public void unlink(String providerId) {
        if (adminKey == null || adminKey.isBlank()) {
            log.warn("Kakao admin key not configured; skipping unlink providerId={}", providerId);
            return;
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", providerId);

        try {
            restClient.post()
                    .uri(UNLINK_URL)
                    .header("Authorization", "KakaoAK " + adminKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Kakao unlink success providerId={}", providerId);
        } catch (Exception e) {
            log.warn("Kakao unlink failed providerId={}", providerId, e);
        }
    }
}
