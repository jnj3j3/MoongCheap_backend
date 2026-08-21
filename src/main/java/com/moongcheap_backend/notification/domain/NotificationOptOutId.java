package com.moongcheap_backend.notification.domain;

import java.io.Serializable;
import java.util.Objects;

public class NotificationOptOutId implements Serializable {
    private Long memberId;
    private NotificationType type;

    public NotificationOptOutId() {}
    public NotificationOptOutId(Long memberId, NotificationType type) {
        this.memberId = memberId;
        this.type = type;
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationOptOutId that)) return false;
        return Objects.equals(memberId, that.memberId) && type == that.type;
    }
    @Override public int hashCode() { return Objects.hash(memberId, type); }
}
