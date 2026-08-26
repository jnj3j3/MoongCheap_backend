package com.moongcheap_backend.order.application;

import com.moongcheap_backend.member.application.OrderMemberInfoService;
import com.moongcheap_backend.member.infrastructure.MemberRepository;
import com.moongcheap_backend.member.presentation.dto.OrderMemberInfo;
import com.moongcheap_backend.order.presentation.dto.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderMemberInfoService orderMemberInfoService;

    //주문하기
    public Void createOrder(Long memberId, CreateOrderRequest request){
        //회원 상태 검증, 배송지 정보 호출
        OrderMemberInfo orderMemberInfo = orderMemberInfoService.getForOrder(memberId,
            request.shippingAddressId());
        //회원 소비자 키 검증

    }
    //주문조회
    //주문상세조회
    //주문취소


    //공통사용기능들

}
