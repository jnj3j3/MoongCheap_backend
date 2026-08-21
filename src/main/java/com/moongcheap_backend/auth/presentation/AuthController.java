package com.moongcheap_backend.auth.presentation;

import com.moongcheap_backend.auth.presentation.dto.ChangePasswordRequestDto;
import com.moongcheap_backend.auth.presentation.dto.LoginIdAvailabilityResponseDto;
import com.moongcheap_backend.auth.presentation.dto.LoginRequestDto;
import com.moongcheap_backend.auth.presentation.dto.SignUpRequestDto;
import com.moongcheap_backend.auth.presentation.dto.WithdrawRequest;
import com.moongcheap_backend.auth.application.AuthLoginService;
import com.moongcheap_backend.auth.application.AuthSignUpService;
import com.moongcheap_backend.auth.application.PasswordChangeService;
import com.moongcheap_backend.auth.application.WithdrawService;
import com.moongcheap_backend.common.response.ApiResponse;
import com.moongcheap_backend.common.security.SessionPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "회원가입, 로그인, 로그아웃, 비밀번호 변경, 탈퇴")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthSignUpService signUpService;
    private final AuthLoginService loginService;
    private final PasswordChangeService passwordChangeService;
    private final WithdrawService withdrawService;

    @Operation(summary = "아이디 회원가입", description = "Auth-01. 아이디/비밀번호로 신규 계정을 생성한다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@RequestBody @Valid SignUpRequestDto request) {
        Long memberId = signUpService.signUp(request);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("memberId", memberId)));
    }

    @Operation(summary = "아이디 중복 검사", description = "Auth-03. 정규화된 아이디 기준으로 활성 회원 중복 여부를 반환한다.")
    @GetMapping("/login-id-availability")
    public ResponseEntity<ApiResponse<LoginIdAvailabilityResponseDto>> checkLoginId(@RequestParam String loginId) {
        return ResponseEntity.ok(ApiResponse.ok(signUpService.checkLoginId(loginId)));
    }

    @Operation(summary = "아이디 로그인", description = "Auth-04. 성공 시 세션 ID 재발급 후 SID 쿠키 발급.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SessionPrincipal>> login(@RequestBody @Valid LoginRequestDto request,
                                                               HttpServletRequest httpRequest) {
        return ResponseEntity.ok(ApiResponse.ok(loginService.login(request, httpRequest)));
    }

    @Operation(summary = "로그아웃", description = "Auth-06. 현재 세션을 무효화한다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest) {
        loginService.logout(httpRequest);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "비밀번호 변경", description = "Auth-08. 현재 세션만 유지하고 다른 세션은 무효화한다.")
    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> edit(SessionPrincipal principal,
                                                  @RequestBody @Valid ChangePasswordRequestDto request,
                                                  HttpServletRequest httpRequest) {
        passwordChangeService.changePassword(principal.memberId(), request, httpRequest);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    /*
     local credential의 경우 비밀번호 확인이 필요하지만, social credential의 경우 비밀번호 확인이
     불필요하기 때문에 request body를 required false 했습니다.
     */
    /*
    추가적으로 보안적 위험 사항이 있습니다. social credential의 경우 비밀번호 검증 없이 바로 delete되기 때문에
    email 인증이 추가로 필요합니다. 하지만 이는 인프라 이후 aws를 이용한 email send가 불가피함으로 추후 추가될 예정입니다.
     */
    @Operation(summary = "회원 탈퇴", description = "Auth-11. 비밀번호 재확인 후 전 세션 삭제·개인정보 파기.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> delete(SessionPrincipal principal,
                                                    @RequestBody(required = false) WithdrawRequest request) {
        withdrawService.withdraw(principal.memberId(), request);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
