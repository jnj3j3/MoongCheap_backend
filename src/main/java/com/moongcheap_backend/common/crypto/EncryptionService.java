package com.moongcheap_backend.common.crypto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncryptionService {

    private final TextEncryptor textEncryptor;

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            return null;
        }
        return textEncryptor.encrypt(plainText);
    }

    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isBlank()) {
            return null;
        }
        return textEncryptor.decrypt(cipherText);
    }

    public String maskAccountNumber(String plainAccountNumber) {
        if (plainAccountNumber == null || plainAccountNumber.length() < 4) {
            return "****";
        }
        int len = plainAccountNumber.length();
        return "*".repeat(len - 4) + plainAccountNumber.substring(len - 4);
    }

    public String maskBusinessNumber(String digits) {
        if (digits == null || digits.length() != 10) return "***-**-*****";
        return digits.substring(0, 3) + "-" + digits.substring(3, 5) + "-" + digits.substring(5, 7) + "***";
    }

    public String maskPhoneNumber(String plainPhoneNumber) {
        if (plainPhoneNumber == null || plainPhoneNumber.length() < 8) {
            return "****";
        }
        String digits = plainPhoneNumber.replaceAll("[^0-9]", "");
        if (digits.length() == 11) {
            return digits.substring(0, 3) + "-****-" + digits.substring(7);
        }
        if (digits.length() == 10) {
            return digits.substring(0, 3) + "-***-" + digits.substring(6);
        }
        return digits.substring(0, 3) + "****" + digits.substring(digits.length() - 4);
    }
}
