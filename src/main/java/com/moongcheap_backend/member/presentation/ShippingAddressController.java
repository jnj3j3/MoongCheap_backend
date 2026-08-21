package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.common.response.ApiResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressEditRequest;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressRequest;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressResponse;
import com.moongcheap_backend.member.application.ShippingAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "User · 배송지", description = "배송지 CRUD 및 기본 배송지 지정")
@RestController
@RequestMapping("/api/shipping-addresses")
@RequiredArgsConstructor
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    @Operation(summary = "배송지 목록", description = "User-05. 기본 배송지 우선, 최근 등록순 정렬.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingAddressResponse>>> list(SessionPrincipal principal) {
        return ResponseEntity.ok(ApiResponse.ok(shippingAddressService.getAll(principal.memberId())));
    }

    @Operation(summary = "배송지 상세", description = "본인 소유 배송지 단건 조회.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingAddressResponse>> detail(SessionPrincipal principal, @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(shippingAddressService.getById(principal.memberId(), id)));
    }

    @Operation(summary = "배송지 등록", description = "User-04. 최대 5개. 첫 배송지는 자동으로 기본으로 지정.")
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(SessionPrincipal principal,
                                                                 @RequestBody @Valid ShippingAddressRequest request) {
        Long id = shippingAddressService.create(principal.memberId(), request);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("id", id)));
    }

    @Operation(summary = "배송지 수정", description = "User-05. 본인 소유 배송지에만 허용.")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> edit(SessionPrincipal principal, @PathVariable Long id,
                                                  @RequestBody @Valid ShippingAddressEditRequest request) {
        shippingAddressService.edit(principal.memberId(), id, request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "배송지 삭제", description = "User-05. 물리 삭제. 기본 배송지도 자동 승격하지 않음.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(SessionPrincipal principal, @PathVariable Long id) {
        shippingAddressService.delete(principal.memberId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "기본 배송지 지정", description = "User-06. 기존 기본 해제 후 새 기본으로 전환 (단일 트랜잭션).")
    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<Void>> setDefault(SessionPrincipal principal, @PathVariable Long id) {
        shippingAddressService.markAsDefault(principal.memberId(), id);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
