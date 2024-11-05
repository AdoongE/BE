package com.adoonge.seedzip.oauth.service;

import com.adoonge.seedzip.auth.dto.request.LoginRequest;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService implements OAuthService {

    private Map<String, Object> getUserAttributesByToken(String accessCode){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessCode))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @Override
    public String getLoginId(String accessToken) {
        return getUserAttributesByToken(accessToken).get("id").toString();
    }

    @Override
    public String getProfileImageUrl(String accessCode) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) getUserAttributesByToken(accessCode).get("kakao_account");
        if(kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if(profile != null) {
                return profile.get("profile_image_url").toString();
            } else {
                return ""; //추후에 기본 프로필 이미지 링크 추가
            }
        }
        return null; ////추후에 기본 프로필 이미지 링크 추가
    }

}
