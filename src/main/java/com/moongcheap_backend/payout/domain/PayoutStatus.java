package com.moongcheap_backend.payout.domain;

public enum PayoutStatus {
    REQUESTED, //지급이 요청되었지만 아직 처리되지 않은 상태입니다. REQUESTED 상태일 때만 지급대행 요청을 취소할 수 있습니다.
    IN_PROGRESS, //지급을 처리하고 있는 상태입니다.
    COMPLETED, //셀러에 지급이 완료된 상태입니다.
    FAILED, //지급 요청이 실패한 상태입니다.
    CANCELED, //지급 요청이 취소된 상태입니다.
    REJECTED //지급 요청이 반려된 상태입니다. 웹훅은 FAILED 상태로 발송됩니다.
}
