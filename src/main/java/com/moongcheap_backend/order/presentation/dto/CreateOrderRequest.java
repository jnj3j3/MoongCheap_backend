package com.moongcheap_backend.order.presentation.dto;

public record CreateOrderRequest(
    Long brandPayId,
    Long groupBuyId,
    Integer totalAmount,
    Long productId,
    Integer sum,
    Integer price,
    Long shippingAddressId
) {

}
