package com.moongcheap_backend.order.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.payments.domain.BrandPayMethod;
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
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //자동결제수단 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brandpay_id")
    private BrandPayMethod brandPayMethod;

    //구매자 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member customer;

    //공동구매 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_buy_id")
    private GroupBuy groupBuy;

    //멱등키
    @Column(name = "idempotency_key",  unique = true)
    private String idempotencyKey;

    //영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열
    @Column(name = "order_no", unique = true)
    private String orderNo;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "products_id")
    private String productsId;

    @Column(name = "products_name")
    private String productsName;

    //수량
    @Column(name = "sum")
    private Integer sum;

    @Column(name = "price")
    private Integer price;

    //수령인 이름
    @Column(name = "shipping_name")
    private String shippingName;

    //수령인 전화번호
    @Column(name = "shipping_number")
    private String shippingNumber;

    //주문상태
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "seller_id")
    private Long sellerId;

    //상호명
    @Column(name = "business_name")
    private String businessName;

    //우편번호
    @Column(name = "zipcode")
    private String zipcode;

    //기본주소
    @Column(name = "address")
    private String address;

    //상세주소
    @Column(name = "address_detail")
    private String addressDetail;

    //상품 이미지 url
    @Column(name = "image_url")
    private String imageUrl;
}
