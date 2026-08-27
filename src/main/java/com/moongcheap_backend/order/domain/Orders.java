package com.moongcheap_backend.order.domain;

import com.moongcheap_backend.common.entity.BaseTimeEntity;
import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.member.domain.Member;
import com.moongcheap_backend.member.domain.Seller;
import com.moongcheap_backend.payments.domain.BrandPayMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @JoinColumn(name = "brandpay_id", nullable = false)
    private BrandPayMethod brandPayMethod;

    //구매자 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Member customer;

    //공동구매 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_buy_id", nullable = false)
    private GroupBuy groupBuy;

    //멱등키
    //대충 UUID사용하면 될 듯
    @Column(name = "idempotency_key", unique = true, length = 255)
    private String idempotencyKey;

    //영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열
    @Column(name = "order_no", nullable = false, unique = true, length = 64)
    private String orderNo;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    //나주에 product테이블이 생기면 그때 외래키로 전환
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    //수량
    @Column(name = "sum", nullable = false)
    private Integer sum;

    @Column(name = "price", nullable = false)
    private Integer price;

    //수령인 이름
    @Column(name = "shipping_name", nullable = false,  length = 50)
    private String shippingName;

    //택배 송장번호
    @Column(name = "shipping_number")
    private String shippingNumber;

    //주문상태
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 30)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id",  nullable = false)
    private Seller seller;

    //상호명
    @Column(name = "business_name", nullable = false,  length = 50)
    private String businessName;

    //우편번호
    @Column(name = "zipcode", nullable = false, length = 5)
    private String zipcode;

    //기본주소
    @Column(name = "address", nullable = false)
    private String address;

    //상세주소
    @Column(name = "address_detail", nullable = false,  length = 100)
    private String addressDetail;

    //배송메모
    @Column(name = "shipping_memo")
    private String shippingMemo;

    //상품 이미지 url
    @Column(name = "image_url")
    private String imageUrl;
}
