package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(example = """
        {
          "id": 1,
          "alias": "집",
          "recipientName": "홍길동",
          "phoneNumberMasked": "010-****-5678",
          "zipcode": "06235",
          "address": "서울특별시 강남구 테헤란로 427",
          "addressDetail": "101동 202호",
          "requestMessage": "문 앞에 놓아주세요",
          "isDefault": true
        }
        """)
public record ShippingAddressResponse(
        Long id,
        String alias,
        String recipientName,
        String phoneNumberMasked,
        String zipcode,
        String address,
        String addressDetail,
        String requestMessage,
        boolean isDefault
) {
}
