package com.moongcheap_backend.auth.presentation;

import com.moongcheap_backend.auth.application.SocialLinkService;
import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.common.response.ApiResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.domain.SocialProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Auth · 소셜 연동", description = "소셜 로그인 계정 연동/해제")
@RestController
@RequestMapping("/api/auth/social-links")
@RequiredArgsConstructor
public class SocialLinkController {

    private final SocialLinkService socialLinkService;

    @Operation(summary = "소셜 계정 연동 시작", description = "Auth-07. OAuth2 인가 페이지로 리다이렉트. 로그인 상태에서 호출해야 연동으로 처리됨.")
    @GetMapping("/{provider}")
    public void link(@PathVariable String provider, HttpServletResponse response) throws IOException {
        resolveProvider(provider);
        response.sendRedirect("/oauth2/authorization/" + provider.toLowerCase());
    }

    @Operation(summary = "소셜 계정 연동 해제", description = "Auth-09. 남는 로그인 수단이 최소 1개일 때만 허용.")
    @DeleteMapping("/{provider}")
    public ResponseEntity<ApiResponse<Void>> delete(SessionPrincipal principal, @PathVariable String provider) {
        socialLinkService.unlink(principal.memberId(), resolveProvider(provider));
        return ResponseEntity.ok(ApiResponse.ok());
    }

    private SocialProvider resolveProvider(String provider) {
        try {
            return SocialProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "지원하지 않는 소셜 제공자입니다: " + provider);
        }
    }
}
