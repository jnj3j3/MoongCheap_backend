package com.moongcheap_backend.groupbuy.domain;

public enum GroupBuyStatus {
    OPEN, //공동구매 오픈
    RECRUITMENT_COMPLETED, //공동구매 성사
    //PAYMENT_COMPLETED, //일괄 결제 완료
    CLOSED, //공동구매 종료
    FAILED, //공동구매 실패
    CANCELED //공동구매 취소
}
