package com.moongcheap_backend.payments.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name = "payments_webhook")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentsWebhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transmission_id", unique = true)
    private String transmissionId;

    @Column(name = "event_type", length = 50)
    private String eventType;

    @Column(name = "toss_created_at")
    private LocalDateTime tossCreatedAt;

    @Column(name = "customer_key", length = 50)
    private String customerKey;

    @Column(name = "method_key")
    private String methodKey;

    @Column(name = "payment_key", length = 200)
    private String paymentKey;

    //주문번호
    @Column(name = "order_id", length = 64)
    private String orderId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "json")
    private String payload;

    //수신일시
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    //처리일시
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
}
