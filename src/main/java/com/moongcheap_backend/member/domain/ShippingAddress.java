package com.moongcheap_backend.member.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "shipping_address",
        indexes = @Index(name = "ix_shipping_address_member_id", columnList = "member_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingAddress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "alias", nullable = false, length = 30)
    private String alias;

    @Column(name = "recipient_name", nullable = false, length = 50)
    private String recipientName;

    @Column(name = "phone_number", nullable = false, length = 512)
    private String phoneNumber;

    @Column(name = "zipcode", nullable = false, length = 5)
    private String zipcode;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "address_detail", length = 100)
    private String addressDetail;

    @Column(name = "request_message", length = 100)
    private String requestMessage;

    @Builder
    private ShippingAddress(Long memberId, String alias, String recipientName, String phoneNumber,
                            String zipcode, String address, String addressDetail,
                            String requestMessage, Boolean isDefault) {
        this.memberId = memberId;
        this.alias = alias;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.requestMessage = requestMessage;
        this.isDefault = isDefault;
    }

    public void update(String alias, String recipientName, String phoneNumber,
                       String zipcode, String address, String addressDetail, String requestMessage) {
        this.alias = alias;
        this.recipientName = recipientName;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.requestMessage = requestMessage;
    }

    public void markAsDefault() {
        this.isDefault = true;
    }

    public void unmarkDefault() {
        this.isDefault = false;
    }

    public boolean isDefault() {
        return Boolean.TRUE.equals(isDefault);
    }
}
