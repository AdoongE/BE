package com.adoonge.seedzip.oauth.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface OAuthService {
    ResponseEntity<Map> requestSocialUserAccessToken(String code);
    ResponseEntity<Map> requestSocialUserAccessInfo(String socialAccessToken);
    String getAccessToken(String socialCode); //state는 네이버 로그인시만 필요
    String getLoginId(String socialAccessToken);
    String getProfileImageUrl(String socialAccessToken);
}
