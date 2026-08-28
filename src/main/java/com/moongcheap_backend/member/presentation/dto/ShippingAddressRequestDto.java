package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(example = """
        {
          "alias": "집",
          "recipientName": "홍길동",
          "phoneNumber": "010-1234-5678",
          "zipcode": "06235",
          "address": "서울특별시 강남구 테헤란로 427",
          "addressDetail": "101동 202호",
          "entranceCode": "1234#",
          "requestMessage": "문 앞에 놓아주세요",
          "setAsDefault": true
        }
        """)
public record ShippingAddressRequestDto(
        @NotBlank @Size(max = 20) String alias,
        @NotBlank @Pattern(regexp = "^[가-힣a-zA-Z\\s]{2,20}$") String recipientName,
        @NotBlank @Pattern(regexp = "^0\\d{1,2}-?\\d{3,4}-?\\d{4}$") String phoneNumber,
        @NotBlank @Pattern(regexp = "\\d{5}") String zipcode,
        @NotBlank @Size(max = 255) String address,
        @Size(max = 100) String addressDetail,
        @Size(max = 20) String entranceCode,
        @Size(max = 100) String requestMessage,
        boolean setAsDefault
) {
}
