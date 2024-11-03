package com.adoonge.seedzip.auth.dto.request;

import com.adoonge.seedzip.auth.domain.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private SocialType socialType; // 소셜 로그인 제공자 (GOOGLE, KAKAO, NAVER 등)
    private String socialAccessToken; // 소셜 로그인 제공자에 접근할 수 있는 토큰이나 값들
}
