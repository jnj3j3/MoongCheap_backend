package com.moongcheap_backend.order.application;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.groupbuy.domain.GroupBuy;
import com.moongcheap_backend.order.domain.OrderStatus;
import com.moongcheap_backend.order.domain.Orders;
import com.moongcheap_backend.order.infrastructure.OrdersRepository;
import com.moongcheap_backend.order.presentation.OrderController.OrderListTab;
import com.moongcheap_backend.order.presentation.dto.CreateOrderRequest;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse.GroupBuyInfo;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse.PaymentInfo;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse.ProductInfo;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse.ShippingInfo;
import com.moongcheap_backend.order.presentation.dto.OrderListResponse;
import com.moongcheap_backend.order.presentation.dto.OrderShippingAddressRequest;
import com.moongcheap_backend.payments.application.OrderPaymentInfoService;
import com.moongcheap_backend.payments.presentation.dto.OrderPaymentInfo;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderPaymentInfoService orderPaymentInfoService;

    //주문하기
    public Void createOrder(Long memberId, CreateOrderRequest request) {
        //회원 상태 검증
        //회원 소비자 키 검증
        return null;
    }

    //주문목록조회
    @Transactional(readOnly = true)
    public Page<OrderListResponse> viewOrderList(Long memberId, OrderListTab orderListTab,
        Pageable pageable) {
        Page<Orders> orders = switch (orderListTab) {
            case ALL -> ordersRepository.findAllByCustomer_Id(memberId, pageable);
            case IN_PROGRESS -> ordersRepository.findAllByCustomer_IdAndOrderStatusIn(
                memberId,
                Set.of(
                    OrderStatus.PAYMENT_PENDING,
                    OrderStatus.PAYMENT_COMPLETED,
                    OrderStatus.PREPARING_SHIPMENT,
                    OrderStatus.SHIPPED
                ),
                pageable
            );
            case DELIVERED -> ordersRepository.findAllByCustomer_IdAndOrderStatusIn(
                memberId, Set.of(OrderStatus.DELIVERED), pageable
            );
            case COMPLETED -> ordersRepository.findAllByCustomer_IdAndOrderStatusIn(
                memberId, Set.of(OrderStatus.COMPLEDED), pageable
            );
        };

        return orders.map(this::toOrderListResponse);
    }

    //주문상세조회
    @Transactional(readOnly = true)
    public OrderDetailResponse viewOrderDetail(Long memberId, String orderNo) {
        Orders order = ordersRepository.findByOrderNoAndMember_Id(orderNo, memberId).
            orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        GroupBuy groupBuy = order.getGroupBuy();
        OrderPaymentInfo paymentInfo = orderPaymentInfoService.getForOrder(order);

        return new OrderDetailResponse(
            order.getCreatedAt().toLocalDate(),
            orderNo,
            new ProductInfo(
                order.getBusinessName(),
                "택배",
                order.getDeliveryFee(),
                order.getOrderStatus(),
                order.getImageUrl(),
                order.getProductName(),
                order.getSum(),
                order.getPrice(),
                new GroupBuyInfo(
                    groupBuy.getId(),
                    groupBuy.getTitle()
                )
            ),
            new ShippingInfo(
                order.getShippingName(),
                order.getPhoneNumber(),
                combineAddress(order.getAddress(), order.getAddressDetail())
            ),
            new PaymentInfo(
                paymentInfo.productAmount(),
                paymentInfo.deliveryFee(),
                paymentInfo.totalPaymentAmount(),
                paymentInfo.paymentMethod()
            )
        );
    }

    //주문취소
    @Transactional
    public void orderCancel(Long memberId, String orderNo) {
        Orders order = findOrderForUpdate(orderNo, memberId);

        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            return;
        }

        if (order.getOrderStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setOrderStatus(OrderStatus.CANCELED);
    }

    //배송지 입력
    @Transactional
    public Void updateShippingAddress(Long memberId,
        String orderNo,
        OrderShippingAddressRequest request
    ) {
        Orders order = findOrderForUpdate(orderNo, memberId);

        if (order.getOrderStatus() != OrderStatus.PAYMENT_COMPLETED) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_SHIPPING);
        }

        order.updateShipping(
            request.shippingName(),
            request.phoneNumber(),
            request.zipcode(),
            request.address(),
            request.addressDetail(),
            request.shippingMemo()
        );

        return null;
    }



    private String combineAddress(String address, String addressDetail) {
        String baseAddress = address == null || address.isBlank() ? null : address;
        String detailAddress = addressDetail == null || addressDetail.isBlank()
            ? null
            : addressDetail;

        if (baseAddress == null) {
            return detailAddress;
        }
        if (detailAddress == null) {
            return baseAddress;
        }

        return baseAddress + " " + detailAddress;
    }

    private OrderListResponse toOrderListResponse(Orders order) {
        return new OrderListResponse(
            order.getCreatedAt().toLocalDate(),
            order.getOrderNo(),
            order.getBusinessName(),
            order.getOrderStatus(),
            order.getImageUrl(),
            order.getProductName(),
            order.getSum(),
            order.getTotalAmount()
        );
    }

    //주문 검색(수정용)
    private Orders findOrderForUpdate(String orderNo, Long memberId) {
        Orders order = ordersRepository.
            findByOrderNoAndCustomer_Id(orderNo, memberId).
            orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        return order;
    }
}
