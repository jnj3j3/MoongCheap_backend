package com.moongcheap_backend.common.lock;

public final class AdvisoryLockKeys {

    public static final String DEMAND_EXPIRE_BATCH = "batch:demand-expire";

    private AdvisoryLockKeys() {}

    public static String shippingAddressCreate(Long memberId) {
        return "shipping:create:" + memberId;
    }

    public static String credentialWrite(Long memberId) {
        return "credential:write:" + memberId;
    }
}
