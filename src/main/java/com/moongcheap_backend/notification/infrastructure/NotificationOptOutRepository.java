package com.moongcheap_backend.notification.infrastructure;

import com.moongcheap_backend.notification.domain.NotificationOptOut;
import com.moongcheap_backend.notification.domain.NotificationOptOutId;
import com.moongcheap_backend.notification.domain.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationOptOutRepository extends JpaRepository<NotificationOptOut, NotificationOptOutId> {
    List<NotificationOptOut> findAllByMemberId(Long memberId);
    boolean existsByMemberIdAndType(Long memberId, NotificationType type);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM NotificationOptOut o WHERE o.memberId = :memberId AND o.type = :type")
    void deleteByMemberIdAndType(@Param("memberId") Long memberId, @Param("type") NotificationType type);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM NotificationOptOut o WHERE o.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
