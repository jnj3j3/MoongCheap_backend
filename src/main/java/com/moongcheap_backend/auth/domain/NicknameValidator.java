package com.moongcheap_backend.auth.domain;

import com.moongcheap_backend.common.exception.BusinessException;
import com.moongcheap_backend.common.exception.ErrorCode;

public final class NicknameValidator {

    private NicknameValidator() {}

    public static String normalize(String nickname) {
        if (nickname == null) {
            throw new BusinessException(ErrorCode.NICKNAME_INVALID);
        }
        String trimmed = nickname.trim().replaceAll("\\s+", " ");
        if (trimmed.length() < 2 || trimmed.length() > 20) {
            throw new BusinessException(ErrorCode.NICKNAME_INVALID);
        }
        return trimmed;
    }
    // 저장되는 모든 nickname은 소문자
    public static String toKey(String normalized) {
        return normalized.toLowerCase();
    }
}
