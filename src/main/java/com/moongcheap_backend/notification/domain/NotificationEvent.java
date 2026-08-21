package com.moongcheap_backend.notification.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification_event",
        uniqueConstraints = @UniqueConstraint(name = "ux_notification_event_type_key", columnNames = {"type", "event_key"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 40)
    private String type;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "body", nullable = false, length = 500)
    private String body;

    @Column(name = "link_url", length = 255)
    private String linkUrl;

    @Column(name = "is_mandatory", nullable = false)
    private boolean isMandatory;

    @Column(name = "event_key", nullable = false, length = 100)
    private String eventKey;

    @Builder
    private NotificationEvent(String type, String title, String body, String linkUrl,
                               boolean isMandatory, String eventKey) {
        this.type = type;
        this.title = title;
        this.body = body;
        this.linkUrl = linkUrl;
        this.isMandatory = isMandatory;
        this.eventKey = eventKey;
    }
}
