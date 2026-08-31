package com.moongcheap_backend.demand.presentation.demandBoard;

import com.moongcheap_backend.demand.application.demandBoard.DemandBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "DemandBoard · 수요 보드", description = "수요 보드 조회")
@RestController
@RequestMapping("/api/demand-boards")
@RequiredArgsConstructor
public class DemandBoardController {

    private final DemandBoardService demandBoardService;

    @Operation(summary = "수요 보드 존재 여부 확인", description = "카탈로그 ID 기준으로 진행 중인 수요 보드(GB_GATHERING, GB_ACTION_REQUIRED)가 있는지 확인합니다.")
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> exists(@RequestParam Long catalogId) {
        boolean exists = demandBoardService.hasProductBoard(catalogId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
