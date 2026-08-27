package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.member.presentation.dto.SellerPublicResponse;
import com.moongcheap_backend.member.application.SellerPublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Seller · 공개 정보", description = "구매자에게 노출되는 판매자 공개 정보 (통신판매업 표시 의무)")
@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerPublicController {

    private final SellerPublicService sellerPublicService;

    @Operation(summary = "판매자 공개 정보 조회", description = "User-12. 상호명·대표자명·사업자번호(마스킹)·통판번호·사업장 연락처.")
    @GetMapping("/{id}/public")
    public ResponseEntity<SellerPublicResponse> detail(@PathVariable Long id) {
        return ResponseEntity.ok(sellerPublicService.detail(id));
    }
}
