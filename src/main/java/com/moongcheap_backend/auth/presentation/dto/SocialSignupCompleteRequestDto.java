package com.moongcheap_backend.auth.presentation.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record SocialSignupCompleteRequestDto(
    @AssertTrue(message = "이용 약관에 동의해야 합니다.")
    boolean termsAgreed,

    @AssertTrue(message = "개인정보 처리방침에 동의해야 합니다")
    boolean policyAgreed,

    @AssertTrue(message = "만 14세 이상 동의해야 합니다.")
    boolean ageVerified,

    @Size(max = 20, message = "닉네임은 20자 이하이어야 합니다.")
    String nickname
) {

}
