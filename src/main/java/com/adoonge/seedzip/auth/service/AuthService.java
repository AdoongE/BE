package com.adoonge.seedzip.auth.service;

import com.adoonge.seedzip.auth.dto.request.LoginRequest;
import com.adoonge.seedzip.auth.dto.request.SignUpRequest;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.repository.MemberRepository;
import com.adoonge.seedzip.oauth.service.OAuthService;
import com.adoonge.seedzip.oauth.service.OAuthServiceFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenService jwtTokenService;
    private final OAuthServiceFactory oauthServiceFactory;
    private final PasswordEncoder passwordEncoder;

    private Boolean isMemberRegistered(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequest request, HttpServletResponse response) {

        OAuthService oauthService = oauthServiceFactory.getOAuthService(request.getSocialType());

        String loginId = oauthService.getLoginId(request.getSocialAccessToken());

        if(!isMemberRegistered(loginId)) {
            throw SeedzipException.from(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, "default");

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        jwtTokenService.generateToken(authentication, response);
    }

    @Transactional
    public void signUp(SignUpRequest request, HttpServletResponse response) {

        OAuthService oauthService = oauthServiceFactory.getOAuthService(request.getSocialType());

        String loginId = oauthService.getLoginId(request.getSocialAccessToken());

        String profileImageUrl = oauthService.getProfileImageUrl(request.getSocialAccessToken());

        if(isMemberRegistered(loginId)) {
            throw SeedzipException.from(ErrorCode.ACCOUNT_USERNAME_EXIST);
        }

        String encodedPassword = passwordEncoder.encode("default");

        Member member = request.toEntity(loginId, encodedPassword, profileImageUrl);

        memberRepository.save(member);
        memberRepository.flush();

        //토큰 생성
        LoginRequest loginRequest = LoginRequest.builder()
                .socialType(request.getSocialType())
                .socialAccessToken(request.getSocialAccessToken())
                .build();

        login(loginRequest, response);
    }
}
