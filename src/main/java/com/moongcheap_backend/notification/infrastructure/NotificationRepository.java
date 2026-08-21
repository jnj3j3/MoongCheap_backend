package com.moongcheap_backend.notification.infrastructure;

import com.moongcheap_backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMemberIdOrderByIdDesc(Long memberId);
    long countByMemberIdAndIsReadFalse(Long memberId);
}
