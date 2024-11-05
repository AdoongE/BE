package com.adoonge.seedzip.oauth.service;

import com.adoonge.seedzip.auth.dto.request.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public interface OAuthService {
    String getLoginId(String socialAccessCode);
    String getProfileImageUrl(String socialAccessCode);
}
