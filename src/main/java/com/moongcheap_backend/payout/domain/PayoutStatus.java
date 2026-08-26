package com.moongcheap_backend.payout.domain;

public enum PayoutStatus {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELED,
    REJECTED
}
