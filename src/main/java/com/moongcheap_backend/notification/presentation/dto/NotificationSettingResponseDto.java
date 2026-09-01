package com.moongcheap_backend.notification.presentation.dto;

import com.moongcheap_backend.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "type": "BID_RESULT",
          "description": "낙찰 결과",
          "enabled": true,
          "mandatory": true
        }
        """)
public record NotificationSettingResponseDto(
        NotificationType type,
        String description,
        boolean enabled,
        boolean mandatory
) {
}
