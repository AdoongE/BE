package com.adoonge.seedzip.auth.controller;

import com.adoonge.seedzip.auth.domain.SocialType;
import com.adoonge.seedzip.auth.dto.request.SignUpRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
        return authService.login(code, SocialType.BASIC, response);
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인 API", description = "카카오 인가코드를 받고 JWT 토큰을 리턴합니다.")
    ApiResponse<LoginResponse> loginKakao(@RequestParam String code, HttpServletResponse response) {
        return authService.login(code, SocialType.KAKAO, response);
    }

    @PostMapping("/login/kakao/app")
    @Operation(summary = "앱용 카카오 로그인 API", description = "카카오 서버에 접근할 수 있는 accessToken을 받고 JWT 토큰을 리턴합니다.")
    ApiResponse<LoginResponse> loginKakaoForApp(@RequestParam String accessToken, HttpServletResponse response) {
        return authService.loginForApp(accessToken, SocialType.KAKAO, response);
    }

    @PostMapping("/login/naver")
    @Operation(summary = "네이버 로그인 API", description = "네이버 인가코드를 받고 JWT 토큰을 리턴합니다.")
    ApiResponse<LoginResponse> loginNaver(@RequestParam String code, HttpServletResponse response) {
        return authService.login(code, SocialType.NAVER, response);
    }

    @PostMapping("/login/naver/app")
    @Operation(summary = "앱용 네이버 로그인 API", description = "네이버 인가코드를 받고 JWT 토큰을 리턴합니다.")
    ApiResponse<LoginResponse> loginNaverForApp(@RequestParam String accessToken, HttpServletResponse response) {
        return authService.loginForApp(accessToken, SocialType.NAVER, response);
    }

    @PostMapping("login/google")
    @Operation(summary = "구글 로그인 API", description = "구글 인가코드를 받고 JWT 토큰을 리턴합니다.")
    ApiResponse<LoginResponse> loginGoogle(@RequestParam String code, HttpServletResponse response) {
        return authService.login(code, SocialType.GOOGLE, response);
    }

    @GetMapping("/test")
    @Operation(summary = "로그인 테스트 API", description = "로그인 여부를 확인할 수 있는 API입니다. 회원의 닉네임을 리턴합니다.")
    public ApiResponse<String> test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();

        String result = member.getNickname();
        return new ApiResponse<>(result);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API", description = "회원가입을 진행하는 API입니다. (SocialType : BASIC, GOOGLE, NAVER, KAKAO")
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }


}
