package com.moongcheap_backend.order.application;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;
import com.moongcheap_backend.member.application.OrderMemberInfoService;
import com.moongcheap_backend.member.infrastructure.MemberRepository;
import com.moongcheap_backend.member.presentation.dto.OrderMemberInfoDto;
import com.moongcheap_backend.order.domain.OrderStatus;
import com.moongcheap_backend.order.domain.Orders;
import com.moongcheap_backend.order.infrastructure.OrdersRepository;
import com.moongcheap_backend.order.presentation.OrderController.OrderListTab;
import com.moongcheap_backend.order.presentation.dto.CreateOrderRequest;
import com.moongcheap_backend.order.presentation.dto.OrderDetailResponse;
import com.moongcheap_backend.order.presentation.dto.OrderListResponse;
import com.moongcheap_backend.order.presentation.dto.OrderShippingAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderMemberInfoService orderMemberInfoService;
    private final OrdersRepository ordersRepository;

    //주문하기
    public Void createOrder(Long memberId, CreateOrderRequest request) {
        //회원 상태 검증, 배송지 정보 호출
        OrderMemberInfoDto orderMemberInfo = orderMemberInfoService.getForOrder(memberId,
            request.shippingAddressId());
        //회원 소비자 키 검증
        return null;
    }

    //주문목록조회
    public Page<OrderListResponse> viewOrderList(Long memberId, OrderListTab orderListTab,
        Pageable pageable) {
        return Page.empty(pageable);
    }

    //주문상세조회
    public OrderDetailResponse viewOrderDetail(Long memberId, String orderNo) {
        return null;
    }

    //주문취소
    @Transactional
    public void orderCancel(Long memberId, String orderNo) {
        Orders order = ordersRepository.findByOrderNoAndCustomer_Id(orderNo, memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            return;
        }

        if (order.getOrderStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setOrderStatus(OrderStatus.CANCELED);
    }

    //배송지 입력
    public OrderDetailResponse inputShippingAddress(Long memberId,
        OrderShippingAddressRequest request) {
        return null;
    }

    //공통사용기능들

}
