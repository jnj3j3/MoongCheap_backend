package com.moongcheap_backend.order.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrderShippingAddressRequest(
    @NotBlank @Size(min = 2, max = 50) String shippingName,
    @NotBlank @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$") String phoneNumber,
    @NotBlank @Pattern(regexp = "\\d{5}") String zipcode,
    @NotBlank @Size(max = 255) String address,
    @NotBlank @Size(max = 100) String addressDetail,
    @Size(max = 100) String shippingMemo
) {

}
