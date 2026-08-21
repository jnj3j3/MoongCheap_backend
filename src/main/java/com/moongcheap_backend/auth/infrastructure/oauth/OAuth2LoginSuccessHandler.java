package com.moongcheap_backend.auth.infrastructure.oauth;

import com.moongcheap_backend.auth.infrastructure.session.AuthSessionManager;
import com.moongcheap_backend.common.security.SessionPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthSessionManager sessionManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        SessionPrincipal principal = oauth2User.getAttribute(CustomOAuth2UserService.ATTR_PRINCIPAL);
        if (principal == null) {
            throw new IllegalStateException("OAuth2 로그인 후 SessionPrincipal 속성을 찾지 못했습니다.");
        }

        String googleAccessToken = extractGoogleAccessTokenIfApplicable(authentication, request);

        sessionManager.bindPrincipal(request, principal, false);

        if (googleAccessToken != null) {
            request.getSession().setAttribute(AuthSessionManager.GOOGLE_ACCESS_TOKEN_ATTR, googleAccessToken);
        }
    }

    private String extractGoogleAccessTokenIfApplicable(Authentication authentication, HttpServletRequest request) {
        if (!(authentication instanceof OAuth2AuthenticationToken oauth2Auth)) return null;
        if (!"google".equalsIgnoreCase(oauth2Auth.getAuthorizedClientRegistrationId())) return null;
        OAuth2AuthorizedClient client = authorizedClientRepository.loadAuthorizedClient(
                oauth2Auth.getAuthorizedClientRegistrationId(), oauth2Auth, request);
        return client != null ? client.getAccessToken().getTokenValue() : null;
    }
}
