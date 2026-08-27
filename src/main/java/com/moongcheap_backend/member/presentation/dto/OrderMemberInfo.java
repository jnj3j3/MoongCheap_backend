package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "nickname": "홍길동",
          "buyerPhoneNumber": "010-****-5678",
          "shipping": {
            "recipientName": "홍길동",
            "phoneNumber": "010-****-5678",
            "zipcode": "06235",
            "address": "서울특별시 강남구 테헤란로 427",
            "addressDetail": "101동 202호",
            "entranceCode": "1234#",
            "requestMessage": "문 앞에 놓아주세요"
          }
        }
        """)
public record OrderMemberInfo(
        String nickname,
        String buyerPhoneNumber,
        ShippingSnapshot shipping
) {
    public record ShippingSnapshot(
            String recipientName,
            String phoneNumber,
            String zipcode,
            String address,
            String addressDetail,
            String entranceCode,
            String requestMessage
    ) {}
}
