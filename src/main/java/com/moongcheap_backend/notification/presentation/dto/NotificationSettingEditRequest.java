package com.moongcheap_backend.notification.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(example = """
        {
          "enabled": false
        }
        """)
public record NotificationSettingEditRequest(
        @NotNull Boolean enabled
) {
}
