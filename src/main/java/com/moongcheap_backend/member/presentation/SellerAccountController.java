package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.common.response.ApiResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.presentation.dto.AccountEditRequest;
import com.moongcheap_backend.member.presentation.dto.AccountResponse;
import com.moongcheap_backend.member.application.SellerAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    현재 sellerInfo table이 생성되어 삭제될 가능성이 높습니다.
 */
@Tag(name = "Seller · 정산 계좌", description = "판매자 정산 계좌 조회/변경")
@RestController
@RequestMapping("/api/sellers/me/account")
@RequiredArgsConstructor
public class SellerAccountController {

    private final SellerAccountService sellerAccountService;

    @Operation(summary = "정산 계좌 조회", description = "User-10. 계좌번호는 뒤 4자리만 노출.")
    @GetMapping
    public ResponseEntity<ApiResponse<AccountResponse>> detail(SessionPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(sellerAccountService.detail(principal.memberId())));
    }

    @Operation(summary = "정산 계좌 변경", description = "User-10. 비밀번호 재확인 필수. 변경 이력 저장.")
    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> edit(SessionPrincipal principal, @RequestBody @Valid AccountEditRequest request) {
        sellerAccountService.edit(principal.memberId(), request);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
