package com.moongcheap_backend.auth.domain;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;

public final class PasswordValidator {

    private PasswordValidator() {}

    public static void validate(String password, String loginId) {
        if (password == null || password.length() < 8 || password.length() > 64) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }
        int kinds = 0;
        if (password.chars().anyMatch(Character::isLetter)) kinds++;
        if (password.chars().anyMatch(Character::isDigit)) kinds++;
        if (password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) kinds++;
        if (kinds < 2) {
            throw new BusinessException(ErrorCode.PASSWORD_INVALID);
        }

        if (loginId != null && !loginId.isBlank() && password.toLowerCase().contains(loginId.toLowerCase())) {
            throw new BusinessException(ErrorCode.PASSWORD_CONTAINS_LOGIN_ID);
        }
    }
}
