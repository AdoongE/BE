package com.adoonge.seedzip.oauth.service;

import com.adoonge.seedzip.auth.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicService implements OAuthService{

    @Override
    public String getLoginId(String accessToken) {
        return accessToken;
    }

    @Override
    public String getProfileImageUrl(String socialAccessToken) {
        //추후에 기본 프로필 이미지 링크 추가
        return "";
    }
}
