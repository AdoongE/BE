package com.adoonge.seedzip.oauth.service;

import org.springframework.stereotype.Service;

@Service
public interface OAuthService {
    String getAccessToken(String socialCode); //state는 네이버 로그인시만 필요
    String getLoginId(String socialAccessToken);
    String getProfileImageUrl(String socialAccessToken);
}
