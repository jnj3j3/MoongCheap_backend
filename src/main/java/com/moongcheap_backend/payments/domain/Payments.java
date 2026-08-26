package com.moongcheap_backend.payments.domain;

import com.moongcheap_backend.order.domain.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "brand_pay_method")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "payments_status")
    private PaymentsStatus status;

    @Column(name = "payments_type")
    private PaymentType type;

    @Column(name = "payments_method")
    private PaymentsMethod method;
}
