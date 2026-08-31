package com.moongcheap_backend.demand.presentation.demand;

import com.moongcheap_backend.common.response.IdResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.demand.application.demand.DemandService;
import com.moongcheap_backend.demand.presentation.demand.dto.DemandCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Demand · 수요 관리", description = "수요 CRUD")
@RestController
@RequestMapping("/api/members/me/demand")
@RequiredArgsConstructor
public class DemandController {

    private final DemandService demandService;

    @Operation(summary = "수요 등록", description = "FN-B09-02. 동일 카탈로그에 진행 중인 수요가 있으면 409.")
    @PostMapping
    public ResponseEntity<IdResponse> create(SessionPrincipal principal,
                                             @RequestBody @Valid DemandCreateRequestDto request) {
        return ResponseEntity.ok(IdResponse.of(demandService.create(request, principal.memberId())));
    }
}
