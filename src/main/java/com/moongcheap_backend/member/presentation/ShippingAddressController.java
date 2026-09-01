package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressEditRequestDto;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressRequestDto;
import com.moongcheap_backend.member.presentation.dto.ShippingAddressResponseDto;
import com.moongcheap_backend.member.application.ShippingAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moongcheap_backend.common.response.IdResponse;

import java.util.List;

@Tag(name = "User · 배송지", description = "배송지 CRUD 및 기본 배송지 지정")
@RestController
@RequestMapping("/api/shipping-addresses")
@RequiredArgsConstructor
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    @Operation(summary = "배송지 목록", description = "BR-B30-01. 기본 배송지 우선, 최근 등록순 정렬.")
    @GetMapping
    public ResponseEntity<List<ShippingAddressResponseDto>> list(SessionPrincipal principal) {
        return ResponseEntity.ok(shippingAddressService.getAll(principal.memberId()));
    }

    @Operation(summary = "배송지 상세", description = "FN-B30-01. 본인 소유 배송지 단건 조회.")
    @GetMapping("/{id}")
    public ResponseEntity<ShippingAddressResponseDto> detail(SessionPrincipal principal, @PathVariable Long id) {
        return ResponseEntity.ok(shippingAddressService.getById(principal.memberId(), id));
    }

    @Operation(summary = "배송지 등록", description = "FN-B30-02. 최대 5개. 첫 배송지는 자동으로 기본으로 지정.")
    @PostMapping
    public ResponseEntity<IdResponse> create(SessionPrincipal principal,
                                             @RequestBody @Valid ShippingAddressRequestDto request) {
        return ResponseEntity.ok(IdResponse.of(shippingAddressService.create(principal.memberId(), request)));
    }

    @Operation(summary = "배송지 수정", description = "FN-B30-02. 본인 소유 배송지에만 허용.")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> edit(SessionPrincipal principal, @PathVariable Long id,
                                     @RequestBody @Valid ShippingAddressEditRequestDto request) {
        shippingAddressService.edit(principal.memberId(), id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "배송지 삭제", description = "FN-B30-01. 물리 삭제. 기본 배송지는 다른 배송지가 존재한다면 자동 승격.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(SessionPrincipal principal, @PathVariable Long id) {
        shippingAddressService.delete(principal.memberId(), id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "기본 배송지 지정", description = "FN-B30-02. 기존 기본 해제 후 새 기본으로 전환 (단일 트랜잭션).")
    @PatchMapping("/{id}/default")
    public ResponseEntity<Void> setDefault(SessionPrincipal principal, @PathVariable Long id) {
        shippingAddressService.markAsDefault(principal.memberId(), id);
        return ResponseEntity.noContent().build();
    }
}
