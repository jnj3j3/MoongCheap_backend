package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.auth.application.NicknameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "User · 닉네임", description = "닉네임 중복 검사")
@RestController
@RequestMapping("/api/members/nicknames")
@RequiredArgsConstructor
public class NicknameController {

    private final NicknameService nicknameService;

    @Operation(summary = "닉네임 중복 검사", description = "User-03. 정규화 후 활성 회원 기준 중복 여부 반환.")
    @GetMapping("/availability")
    public ResponseEntity<Map<String, Object>> detail(@RequestParam String nickname) {
        boolean available = nicknameService.isAvailable(nickname);
        return ResponseEntity.ok(Map.of("nickname", nickname, "available", available));
    }
}
