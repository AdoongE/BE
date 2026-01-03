package com.adoonge.seedzip.auth.service;

import com.adoonge.seedzip.auth.domain.SocialType;
import com.adoonge.seedzip.auth.dto.request.BasicLoginRequest;
import com.adoonge.seedzip.auth.dto.request.BasicSignUpRequest;
import com.adoonge.seedzip.auth.dto.request.SignUpRequest;
import com.adoonge.seedzip.auth.dto.response.BasicLoginResponse;
import com.adoonge.seedzip.auth.dto.response.LoginResponse;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.repository.MemberRepository;
import com.adoonge.seedzip.auth.service.oauth.OAuthService;
import com.adoonge.seedzip.auth.service.oauth.OAuthServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;
    private final OAuthServiceFactory oauthServiceFactory;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    private Boolean isMemberRegistered(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public ApiResponse<LoginResponse> login(String code, String input, HttpServletResponse response) {
        SocialType socialType;
        try{
            socialType = SocialType.valueOf(input);
        } catch (IllegalStateException e) {
            throw SeedzipException.from(ErrorCode.INVALID_SOCIAL_CODE);
        }

        OAuthService oauthService = oauthServiceFactory.getOAuthService(socialType);

        String accessToken = oauthService.getAccessToken(code);

        String loginId = oauthService.getLoginId(accessToken);

        if (!isMemberRegistered(loginId)) {
            return new ApiResponse<>(LoginResponse.builder().result(accessToken).socialType(socialType).build(), ErrorCode.MEMBER_NOT_FOUND);
        }

        generateToken(loginId, response);

        return new ApiResponse<>(LoginResponse.builder().result("").socialType(socialType).build(), ErrorCode.REQUEST_OK);
    }

    @Transactional(readOnly = true)
    public ApiResponse<LoginResponse> loginForApp(String accessToken, String input, HttpServletResponse response) {
        SocialType socialType;
        try{
            socialType = SocialType.valueOf(input);
        } catch (IllegalStateException e) {
            throw SeedzipException.from(ErrorCode.INVALID_SOCIAL_CODE);
        }

        OAuthService oauthService = oauthServiceFactory.getOAuthService(socialType);

        String loginId = oauthService.getLoginId(accessToken);

        if (!isMemberRegistered(loginId)) {
            return new ApiResponse<>(LoginResponse.builder().result(accessToken).socialType(socialType).build(), ErrorCode.MEMBER_NOT_FOUND);
        }

        generateToken(loginId, response);

        return new ApiResponse<>(LoginResponse.builder().result("").socialType(socialType).build(), ErrorCode.REQUEST_OK);
    }

    @Transactional(readOnly = true)
    public ApiResponse<BasicLoginResponse> basicLogin(BasicLoginRequest basicLoginRequest, HttpServletResponse response) {
        Member member =
            memberRepository
                .findByLoginId(basicLoginRequest.email())
                .orElseThrow(() -> SeedzipException.from(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(basicLoginRequest.password(), member.getPassword())) {
            throw SeedzipException.from(ErrorCode.INVALID_CREDENTIALS);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                basicLoginRequest.email(),
                basicLoginRequest.password()
            );

        Authentication authentication =
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        jwtTokenService.generateToken(authentication, response);

        return new ApiResponse<>(
            BasicLoginResponse.builder()
                .result("")
                .socialType(SocialType.BASIC)
                .build(),
            ErrorCode.REQUEST_OK);
    }

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {

        SocialType socialType;
        try{
            socialType = SocialType.valueOf(request.getSocialType().toUpperCase());
        } catch (IllegalStateException e) {
            throw SeedzipException.from(ErrorCode.INVALID_SOCIAL_CODE);
        }

        OAuthService oauthService = oauthServiceFactory.getOAuthService(socialType);

        String accessToken = request.getAccessToken();

        String loginId = oauthService.getLoginId(accessToken);

        if (isMemberRegistered(loginId)) {
            throw SeedzipException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        String encodedPassword = passwordEncoder.encode("default");

        Member member = request.toEntity(loginId ,encodedPassword, null); // 기본 프로필 이미지 추가해야함

        memberRepository.save(member);
        memberRepository.flush();

        Category category = Category.builder().name("미분류").member(member).isPublic(true).isDefault(true).build();
        categoryRepository.save(category);

       generateToken(loginId, response);
    }

    @Transactional
    public void basicSignUp(BasicSignUpRequest request) {
        String email = request.getEmail();
        if (memberRepository.existsByLoginId(email)) {
            throw SeedzipException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = request.toEntity(email ,encodedPassword, null); // 기본 프로필 이미지 추가해야함

        memberRepository.save(member);
        memberRepository.flush();

        Category category = Category.builder().name("미분류").member(member).isPublic(true).isDefault(true).build();
        categoryRepository.save(category);
    }

    private void generateToken(String loginId, HttpServletResponse response) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, "default");

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        jwtTokenService.generateToken(authentication, response);
    }
}
