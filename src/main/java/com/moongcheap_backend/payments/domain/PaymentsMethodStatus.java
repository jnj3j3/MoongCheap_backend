package com.moongcheap_backend.payments.domain;

public enum PaymentsMethodStatus {
    ACTIVE, //사용 가능
    INACTIVE, //사용 불가
    EXPIRED //삭제된 결제수단
}
