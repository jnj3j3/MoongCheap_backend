package com.moongcheap_backend.notification.infrastructure;

import com.moongcheap_backend.notification.domain.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent, Long> {
    Optional<NotificationEvent> findByTypeAndEventKey(String type, String eventKey);
}
