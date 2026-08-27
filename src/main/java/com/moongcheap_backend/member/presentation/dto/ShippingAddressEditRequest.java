package com.moongcheap_backend.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(example = """
        {
          "alias": "회사",
          "recipientName": "홍길동",
          "phoneNumber": "010-1234-5678",
          "zipcode": "06235",
          "address": "서울특별시 강남구 테헤란로 427",
          "addressDetail": "301동 1001호",
          "entranceCode": null,
          "requestMessage": "경비실에 맡겨주세요"
        }
        """)
public record ShippingAddressEditRequest(
        @NotBlank @Size(max = 30) String alias,
        @NotBlank @Size(min = 2, max = 50) String recipientName,
        @NotBlank String phoneNumber,
        @NotBlank @Pattern(regexp = "\\d{5}") String zipcode,
        @NotBlank @Size(max = 255) String address,
        @Size(max = 100) String addressDetail,
        @Size(max = 20) String entranceCode,
        @Size(max = 100) String requestMessage
) {
}
