package com.moongcheap_backend.order.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.payments.domain.BrandPayMethod;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    // 주문 식별 및 상태
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열
    @Column(name = "order_no", nullable = false, unique = true, length = 64)
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 30)
    @Setter
    private OrderStatus orderStatus;

    // 주문 연관 정보
    // 자동결제수단 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandpay_id")
    private BrandPayMethod brandPayMethod;

    // 구매자 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member customer;

    // 공동구매 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_buy_id", nullable = false)
    private GroupBuy groupBuy;

    // 상품 및 결제 금액
    // 나중에 product 테이블이 생기면 외래키로 전환
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    // 상품 이미지 url
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // 수량
    @Column(name = "sum", nullable = false)
    private Integer sum;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "delivery_fee", nullable = false)
    private Integer deliveryFee;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    // 판매자 스냅샷
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    // 상호명
    @Column(name = "business_name", nullable = false, length = 50)
    private String businessName;

    // 배송지 스냅샷
    // 수령인 이름
    @Column(name = "shipping_name", length = 50)
    private String shippingName;

    // 수령인 전화번호
    @Column(name = "phone_number", columnDefinition = "TEXT")
    private String phoneNumber;

    // 우편번호
    @Column(name = "zipcode", length = 5)
    private String zipcode;

    // 기본주소
    @Column(name = "address")
    private String address;

    // 상세주소
    @Column(name = "address_detail", length = 100)
    private String addressDetail;

    // 배송메모
    @Column(name = "shipping_memo")
    private String shippingMemo;

    // 배송 정보
    // 택배 송장번호
    @Column(name = "shipping_number", columnDefinition = "TEXT")
    private String shippingNumber;

    public Void updateShipping(
        String shippingName,
        String phoneNumber,
        String zipcode,
        String address,
        String addressDetail,
        String shippingMemo) {
        this.shippingName = shippingName;
        this.phoneNumber = phoneNumber;
        this.zipcode = zipcode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.shippingMemo = shippingMemo;
        return null;
    }

}
