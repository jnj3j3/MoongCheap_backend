package com.moongcheap_backend.auth.presentation;

import com.moongcheap_backend.auth.presentation.dto.SellerRegisterRequestDto;
import com.moongcheap_backend.auth.application.SellerRegistrationService;
import com.moongcheap_backend.common.security.SessionPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Seller · 등록", description = "판매자 역할 부여 (Auth-12 / User-07)")
@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerRegistrationController {

    private final SellerRegistrationService sellerRegistrationService;

    @Operation(summary = "판매자 등록", description = "기능 명세 X. 사업자등록번호 형식·체크섬 검증, 즉시 APPROVED, 세션 무효화 없이 권한 갱신.")
    @PostMapping
    public ResponseEntity<Map<String, Long>> create(SessionPrincipal principal,
                                                    @RequestBody @Valid SellerRegisterRequestDto request,
                                                    HttpServletRequest httpRequest) {
        Long sellerId = sellerRegistrationService.register(principal.memberId(), request, httpRequest);
        return ResponseEntity.ok(Map.of("sellerId", sellerId));
    }
}
