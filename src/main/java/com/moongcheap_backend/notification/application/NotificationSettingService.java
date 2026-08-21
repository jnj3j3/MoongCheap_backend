package com.moongcheap_backend.notification.application;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.notification.domain.NotificationOptOut;
import com.moongcheap_backend.notification.domain.NotificationType;
import com.moongcheap_backend.notification.infrastructure.NotificationOptOutRepository;
import com.moongcheap_backend.notification.presentation.dto.NotificationSettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

    private final NotificationOptOutRepository notificationOptOutRepository;

    @Transactional(readOnly = true)
    public List<NotificationSettingResponse> getAll(Long memberId) {
        Set<NotificationType> disabled = notificationOptOutRepository.findAllByMemberId(memberId)
                .stream()
                .map(NotificationOptOut::getType)
                .collect(Collectors.toSet());
        return Arrays.stream(NotificationType.values())
                .map(type -> {
                    boolean enabled = !disabled.contains(type);
                    return new NotificationSettingResponse(type, type.getDescription(), enabled, type.isMandatory());
                })
                .toList();
    }

    @Transactional
    public void edit(Long memberId, NotificationType type, boolean enabled) {
        if (type.isMandatory() && !enabled) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "필수 알림은 해제할 수 없습니다.");
        }
        if (enabled) {
            notificationOptOutRepository.deleteByMemberIdAndType(memberId, type);
        } else if (!notificationOptOutRepository.existsByMemberIdAndType(memberId, type)) {
            notificationOptOutRepository.save(NotificationOptOut.builder()
                    .memberId(memberId)
                    .type(type)
                    .build());
        }
    }
}
