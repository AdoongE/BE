package com.adoonge.seedzip.oauth.service;

import org.springframework.stereotype.Service;

@Service
public interface OAuthService {
    String getAccessToken(String socialCode);
    String getLoginId(String socialAccessToken);
    String getProfileImageUrl(String socialAccessToken);
}
