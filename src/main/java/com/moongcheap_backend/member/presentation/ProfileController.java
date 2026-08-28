package com.moongcheap_backend.member.presentation;

import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.member.presentation.dto.ProfileEditRequestDto;
import com.moongcheap_backend.member.presentation.dto.ProfileResponseDto;
import com.moongcheap_backend.member.application.ProfileService;
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

@Tag(name = "User · 프로필", description = "본인 프로필 조회/수정")
@RestController
@RequestMapping("/api/members/me")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "내 프로필 조회", description = "FN-B24-01. 본인 프로필 상세 (연락처는 마스킹).")
    @GetMapping
    public ResponseEntity<ProfileResponseDto> detail(SessionPrincipal principal) {
        return ResponseEntity.ok(profileService.detail(principal.memberId()));
    }

    @Operation(summary = "내 프로필 수정", description = "BR-B24-01. 닉네임/연락처/이메일/프로필 이미지 PATCH.")
    @PatchMapping
    public ResponseEntity<Void> edit(SessionPrincipal principal, @RequestBody @Valid ProfileEditRequestDto request) {
        profileService.edit(principal.memberId(), request);
        return ResponseEntity.noContent().build();
    }
}
