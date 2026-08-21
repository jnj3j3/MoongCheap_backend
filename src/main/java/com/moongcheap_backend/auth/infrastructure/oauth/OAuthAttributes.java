package com.moongcheap_backend.auth.infrastructure.oauth;

import com.moongcheap_backend.member.domain.SocialProvider;

import java.util.Map;

public record OAuthAttributes(
        SocialProvider provider,
        String providerUserId,
        String email,
        String nickname,
        Map<String, Object> raw
) {

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "kakao" -> ofKakao(attributes);
            case "google" -> ofGoogle(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        };
    }

    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        String id = String.valueOf(attributes.get("id"));
        Map<String, Object> account = (Map<String, Object>) attributes.getOrDefault("kakao_account", Map.of());
        Map<String, Object> profile = (Map<String, Object>) account.getOrDefault("profile", Map.of());
        String nickname = profile.get("nickname") != null ? profile.get("nickname").toString() : null;
        String email = account.get("email") != null ? account.get("email").toString() : null;
        return new OAuthAttributes(SocialProvider.KAKAO, id, email, nickname, attributes);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        String sub = String.valueOf(attributes.get("sub"));
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        return new OAuthAttributes(SocialProvider.GOOGLE, sub, email, name, attributes);
    }
}
