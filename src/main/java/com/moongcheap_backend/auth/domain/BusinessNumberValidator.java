package com.moongcheap_backend.auth.domain;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;

public final class BusinessNumberValidator {

    private static final int[] WEIGHTS = {1, 3, 7, 1, 3, 7, 1, 3, 5};

    private BusinessNumberValidator() {}

    public static String normalizeAndValidate(String businessNumber) {
        if (businessNumber == null) {
            throw new BusinessException(ErrorCode.BUSINESS_NUMBER_INVALID);
        }
        String digits = businessNumber.replaceAll("[^0-9]", "");
        if (digits.length() != 10) {
            throw new BusinessException(ErrorCode.BUSINESS_NUMBER_INVALID);
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * WEIGHTS[i];
        }
        sum += (Character.getNumericValue(digits.charAt(8)) * 5) / 10;
        int check = (10 - (sum % 10)) % 10;
        if (check != Character.getNumericValue(digits.charAt(9))) {
            throw new BusinessException(ErrorCode.BUSINESS_NUMBER_INVALID);
        }
        return digits;
    }
}
