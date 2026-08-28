package com.moongcheap_backend.payout.domain;

public enum SellerPayoutAccountStatus {
    APPROVAL_REQUIRED, //지급대행이 불가능한 상태입니다. 개인 및 개인사업자 셀러 등록 직후의 상태이며, 본인인증이 필요합니다.
    PARTIALLY_APPROVED, //일주일 동안 1천만원까지 지급대행이 가능한 상태입니다. 등록 직후의 법인사업자 셀러 또는 본인인증을 완료한 개인 및 개인사업자 셀러의 상태입니다.
    KYC_REQUIRED, //지급대행이 불가능한 상태입니다. 일주일 동안 1천만원을 초과하는 금액을 지급 요청하면 셀러는 해당 상태로 변경됩니다. 셀러가 KYC 심사를 완료해야 합니다.
    APPROVED //금액 제한 없이 지급대행이 가능한 상태입니다. KYC 심사가 정상적으로 완료된 셀러의 상태입니다.
}
