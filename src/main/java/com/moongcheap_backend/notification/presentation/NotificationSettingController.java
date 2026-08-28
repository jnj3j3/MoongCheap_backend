package com.moongcheap_backend.notification.presentation;

import com.moongcheap_backend.common.security.SessionPrincipal;
import com.moongcheap_backend.notification.domain.NotificationType;
import com.moongcheap_backend.notification.presentation.dto.NotificationSettingEditRequestDto;
import com.moongcheap_backend.notification.presentation.dto.NotificationSettingResponseDto;
import com.moongcheap_backend.notification.application.NotificationSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User · 알림 설정", description = "SSE 인앱 알림 수신 여부 관리")
@RestController
@RequestMapping("/api/members/me/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    @Operation(summary = "알림 수신 설정 목록", description = "MVP 범위 X. 전체 알림 유형과 현재 수신 여부 반환.")
    @GetMapping
    public ResponseEntity<List<NotificationSettingResponseDto>> list(SessionPrincipal principal) {
        return ResponseEntity.ok(notificationSettingService.getAll(principal.memberId()));
    }

    @Operation(summary = "알림 유형별 설정 변경", description = "MVP 범위 X. 거래 필수 알림은 해제 불가.")
    @PatchMapping("/{type}")
    public ResponseEntity<Void> edit(SessionPrincipal principal,
                                     @PathVariable NotificationType type,
                                     @RequestBody @Valid NotificationSettingEditRequestDto request) {
        notificationSettingService.edit(principal.memberId(), type, request.enabled());
        return ResponseEntity.noContent().build();
    }
}
