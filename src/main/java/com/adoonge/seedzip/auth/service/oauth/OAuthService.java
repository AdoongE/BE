package com.adoonge.seedzip.auth.service.oauth;

import org.springframework.stereotype.Service;

@Service
public interface OAuthService {
    String getAccessToken(String code);

    String getLoginId(String accessToken);
}
