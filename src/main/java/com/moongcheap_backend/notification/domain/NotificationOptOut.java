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
@IdClass(NotificationOptOutId.class)
@Table(name = "notification_opt_out")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationOptOut {

    @Id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private NotificationType type;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private NotificationOptOut(Long memberId, NotificationType type) {
        this.memberId = memberId;
        this.type = type;
    }
}
