package com.moongcheap_backend.payout.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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

    @Column(name = "seller_id")
    private Long sellerId;

    //지급요청식별자
    @Column(name = "ref_payout_id", unique = true)
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
    @Column(name = "status")
    private PayoutStatus status;
}
