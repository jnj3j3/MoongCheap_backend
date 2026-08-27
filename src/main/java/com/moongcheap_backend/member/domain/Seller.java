package com.moongcheap_backend.member.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "seller",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_seller_member_id", columnNames = "member_id")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "business_name", nullable = false, length = 50)
    private String businessName;

    @Column(name = "business_number", nullable = false, length = 512)
    private String businessNumber;

    @Column(name = "business_number_hash", nullable = false, length = 64)
    private String businessNumberHash;

    @Column(name = "mail_order_registration_number", nullable = false, length = 30)
    private String mailOrderRegistrationNumber;

    @Column(name = "owner_name", nullable = false, length = 50)
    private String ownerName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seller_status", nullable = false, length = 20)
    private SellerStatus status;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private Seller(Long memberId, String businessName, String businessNumber, String businessNumberHash,
                   String mailOrderRegistrationNumber, String ownerName, String phoneNumber) {
        this.memberId = memberId;
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.businessNumberHash = businessNumberHash;
        this.mailOrderRegistrationNumber = mailOrderRegistrationNumber;
        this.ownerName = ownerName;
        this.phoneNumber = phoneNumber;
        this.status = SellerStatus.PENDING;
    }

    public void approve() {
        this.status = SellerStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.status = SellerStatus.WITHDRAWN;
    }

    // 다음은 seller의 상태만을 추적하기 때문에 deleted는 따로 검사해야합니다. (deletedAt)
    public boolean isSellable() {
        return status == SellerStatus.APPROVED;
    }
}
