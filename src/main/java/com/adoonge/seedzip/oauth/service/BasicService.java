package com.adoonge.seedzip.oauth.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicService implements OAuthService{


    @Override
    public ResponseEntity<Map> requestSocialUserAccessToken(String code) {
        return null;
    }

    @Override
    public ResponseEntity<Map> requestSocialUserInfo(String socialAccessToken) {
        return null;
    }

    @Override
    public String getSocialAccessToken(String code) {
        return code;
    }

    @Override
    public String getLoginId(String loginId) {
        return loginId;
    }

    @Override
    public String getProfileImageUrl(String loginId) {
        //추후에 기본 프로필 이미지 링크 추가
        return "";
    }
}
