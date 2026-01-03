package com.adoonge.seedzip.auth.controller;

import com.adoonge.seedzip.auth.dto.request.BasicLoginRequest;
import com.adoonge.seedzip.auth.dto.request.BasicSignUpRequest;
import com.adoonge.seedzip.auth.dto.request.SignUpRequest;
import com.adoonge.seedzip.auth.dto.response.BasicLoginResponse;
import com.adoonge.seedzip.auth.dto.response.LoginResponse;
import com.adoonge.seedzip.auth.service.AuthService;
import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "회원 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "테스트용 기본 로그인 API입니다.")
    public ApiResponse<LoginResponse> login(@RequestParam String code, HttpServletResponse response) {
        return authService.login(code, "BASIC", response);
    }


    @PostMapping("/login/{socialType}")
    @Operation(summary = "소셜 로그인 API", description = "소셜 인가코드를 받고 JWT 토큰을 리턴합니다. socialType -> {KAKAO, NAVER, GOOGLE}")
    ApiResponse<LoginResponse> socialLogin(@RequestParam String code, @PathVariable String socialType, HttpServletResponse response) {
        return authService.login(code, socialType.toUpperCase(), response);
    }

    @PostMapping("/login/{socialType}/app")
    @Operation(summary = "앱용 소셜 로그인 API", description = "소셜 로그인 서버에 접근할 수 있는 accessToken을 받고 JWT 토큰을 리턴합니다. socialType -> {KAKAO, NAVER, GOOGLE}")
    ApiResponse<LoginResponse> loginKakaoForApp(@RequestParam String accessToken, @PathVariable String socialType, HttpServletResponse response) {
        return authService.loginForApp(accessToken, socialType.toUpperCase(), response);
    }

    @Operation(
        summary = "기본 로그인",
        description = "이메일과 비밀번호로 로그인합니다. '@'를 포함한 이메일과 비밀번호를 입력해주세요.")
    @PostMapping("/login/basic")
    public ApiResponse<BasicLoginResponse>basicLogin(
        @Valid @RequestBody BasicLoginRequest basicLoginRequest, HttpServletResponse response) {

        return authService.basicLogin(basicLoginRequest, response);
    }

    @GetMapping("/test")
    @Operation(summary = "로그인 테스트 API", description = "로그인 여부를 확인할 수 있는 API입니다. 회원의 닉네임을 리턴합니다.")
    public ApiResponse<String> test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        String result = member.getNickname();
        return new ApiResponse<>(result);
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입을 진행하는 API입니다. (SocialType : BASIC, GOOGLE, NAVER, KAKAO")
    public ApiResponse<Void> signUp(
        @RequestBody @Valid SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/signup/basic")
    @Operation(
        summary = "기본 회원가입",
        description = "이메일과 비밀번호로 회원가입합니다.\n"
                    + "- 이메일 : @를 포함한 이메일 형식이어야 합니다.\n"
                    + "- 비밀번호 : 비밀번호는 영문자와 숫자를 포함한 8~20자여야 합니다")
    public ApiResponse<Void> basicSignUp(
        @RequestBody @Valid BasicSignUpRequest basicSignUpRequest, HttpServletResponse response) {

        authService.basicSignUp(basicSignUpRequest);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }


}
