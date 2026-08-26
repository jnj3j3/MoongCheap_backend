package com.moongcheap_backend.payout.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//정산
@Entity
@Getter
@Table(name = "payout")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payout extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    //지급요청식별자
    @Column(name = "ref_payout_id", unique = true, length = 50)
    private String refPayoutId;

    //PG사 판매자 식별자
    @Column(name = "pg_seller_key")
    private String pgSellerKey;

    //지급일
    @Column(name = "payout_date")
    private LocalDateTime payoutDate;

    //정산금액
    @Column(name = "amount")
    private Integer amount;

    //요청시간
    @Column(name = "request_at")
    private LocalDateTime requestAt;

    //정산 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PayoutStatus status;
}
