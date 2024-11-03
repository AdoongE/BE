package com.adoonge.seedzip.auth.controller;

import com.adoonge.seedzip.auth.dto.request.LoginRequest;
import com.adoonge.seedzip.auth.dto.request.SignUpRequest;
import com.adoonge.seedzip.auth.service.AuthService;
import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        authService.login(loginRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signUp(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {

        authService.signUp(signUpRequest, response);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @GetMapping
    public ApiResponse<String> test(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        List<String> result = new ArrayList<>();
        String username = member.getUsername();
        String profileImageUrl = member.getProfileImageUrl();
        result.add(username);
        result.add(profileImageUrl);
        return new ApiResponse<>(result);
    }
}
