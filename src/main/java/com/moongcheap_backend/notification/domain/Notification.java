package com.moongcheap_backend.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notification",
        uniqueConstraints = @UniqueConstraint(name = "ux_notification_member_event", columnNames = {"member_id", "notification_event_id"}),
        indexes = @Index(name = "ix_notification_member_id_desc", columnList = "member_id, id DESC"))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification_event_id", nullable = false)
    private Long notificationEventId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Notification(Long notificationEventId, Long memberId) {
        this.notificationEventId = notificationEventId;
        this.memberId = memberId;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
