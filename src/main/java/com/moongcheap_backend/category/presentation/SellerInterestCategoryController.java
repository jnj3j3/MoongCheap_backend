package com.moongcheap_backend.category.presentation;

import com.moongcheap_backend.common.response.ApiResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.category.presentation.dto.InterestCategoryUpdateRequest;
import com.moongcheap_backend.category.application.SellerInterestCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Seller · 관심 카테고리", description = "판매자 관심 카테고리 조회/수정")
@RestController
@RequestMapping("/api/sellers/me/interest-categories")
@RequiredArgsConstructor
public class SellerInterestCategoryController {

    private final SellerInterestCategoryService sellerInterestCategoryService;

    @Operation(summary = "관심 카테고리 목록", description = "User-08. 현재 지정된 카테고리 ID 목록.")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, List<Long>>>> list(SessionPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(
                Map.of("categoryIds", sellerInterestCategoryService.getAll(principal.memberId()))));
    }

    @Operation(summary = "관심 카테고리 전체 교체", description = "User-08. 최소 1개, 최대 10개. 마지막 1개 삭제 차단.")
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> edit(SessionPrincipal principal,
                                                  @RequestBody @Valid InterestCategoryUpdateRequest request) {
        sellerInterestCategoryService.update(principal.memberId(), request.categoryIds());
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
