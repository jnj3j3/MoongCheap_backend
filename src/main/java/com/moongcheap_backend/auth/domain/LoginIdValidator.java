package com.moongcheap_backend.auth.domain;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;

import java.util.regex.Pattern;

public final class LoginIdValidator {

    private static final Pattern PATTERN = Pattern.compile("^[a-z][a-z0-9_]{3,19}$");

    private LoginIdValidator() {}

    public static String normalizeAndValidate(String loginId) {
        if (loginId == null) {
            throw new BusinessException(ErrorCode.LOGIN_ID_INVALID);
        }
        String normalized = loginId.trim().toLowerCase();
        if (!PATTERN.matcher(normalized).matches()) {
            throw new BusinessException(ErrorCode.LOGIN_ID_INVALID);
        }
        return normalized;
    }
}
