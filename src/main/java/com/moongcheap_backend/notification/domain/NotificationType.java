package com.moongcheap_backend.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    // Buyer
    DEMAND_REGISTERED(false, "수요 등록"),
    DEMAND_MERGED(false, "수요 병합"),
    RECRUIT_STATUS(false, "모집 현황"),
    BID_RESULT(true, "낙찰 결과"),
    PAYMENT_SCHEDULED(true, "결제 예정"),
    PAYMENT_COMPLETED(true, "결제 완료"),
    PAYMENT_FAILED(true, "결제 실패"),
    DELIVERY(true, "배송"),
    CANCELED(true, "무산"),
    // Seller
    BID_COMPETITION(false, "응찰 경쟁 현황"),
    BID_WON(true, "낙찰"),
    BID_LOST(false, "미낙찰"),
    ORDER_CREATED(false, "주문 발생"),
    STOCK_DEPLETED(false, "재고 소진");

    private final boolean mandatory;
    private final String description;
}
