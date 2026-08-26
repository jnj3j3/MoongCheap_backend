package com.moongcheap_backend.payments.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.member.domain.Seller;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//판매자 식별키
@Entity
@Getter
@Table(name = "seller_key")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerKey extends BaseTimeEntity {
    @Id
    private Long sellerId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(name = "seller_key", unique = true)
    private String sellerKey;
}
