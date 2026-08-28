package com.moongcheap_backend.payout.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.member.domain.Seller;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "seller_payout_account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerPayoutAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    //토스에서 판매자 등록시 발급해줌
    @Column(name = "pg_seller_key", unique = true, length = 35)
    private String pgSellerKey;

    //사업자 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false, length = 30)
    private SellerBusinessType businessType;

    //정산 허가 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SellerPayoutAccountStatus status;

    @Column(name = "bank_code", nullable = false, length = 10)
    private String bankCode;

    @Column(name = "bank_account", nullable = false, length = 50)
    private String bankAccount;

    @Column(name = "depositor_name", nullable = false)
    private String depositorName;
}
