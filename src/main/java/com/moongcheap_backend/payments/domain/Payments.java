package com.moongcheap_backend.payments.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.order.domain.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder.Default;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @Column(name = "payment_key", unique = true, length = 200)
    private String paymentKey;

    @Column(name = "order_no", length = 64)
    private String orderNo;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payments_status", nullable = false, length = 30)
    private PaymentsStatus status = PaymentsStatus.READY;

    @Enumerated(EnumType.STRING)
    @Column(name = "payments_type", length = 30)
    private PaymentsType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 30)
    private PaymentsMethod method;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detail", columnDefinition = "jsonb")
    private String detail;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "cancel_reason")
    private String cancelReason;
}
