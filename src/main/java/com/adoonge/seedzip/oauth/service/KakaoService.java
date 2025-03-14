package com.adoonge.seedzip.oauth.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService implements OAuthService {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;
    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<Map> requestSocialUserAccessToken(String code) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiKey);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // POST 요청 실행
        String requestUrl = "https://kauth.kakao.com/oauth/token";
        try {
            return restTemplate.exchange(requestUrl, HttpMethod.POST, request, Map.class);
        } catch (Exception e) {
            throw SeedzipException.from(ErrorCode.OAUTH2_INVALID_CODE);
        }
    }

    @Override
    public ResponseEntity<Map> requestSocialUserInfo(String socialAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + socialAccessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<>(headers);
        String requestUrl = "https://kapi.kakao.com/v2/user/me";

        return restTemplate.exchange(requestUrl, HttpMethod.GET, request, Map.class);
    }

    @Override
    public String getSocialAccessToken(String code) {
        ResponseEntity<Map> response = requestSocialUserAccessToken(code);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return responseBody != null ? (String) responseBody.get("access_token") : null;
        } else {
            throw SeedzipException.from(ErrorCode.OAUTH2_INVALID_CODE);
        }
    }

    @Override
    public String getLoginId(String accessToken) {
        // 1. 카카오 API로 사용자 정보 요청
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user info from Kakao API.");
        }

        Map<String, Object> responseBody = response.getBody();

        return responseBody.get("id").toString();
    }

    @Override
    public String getProfileImageUrl(String accessToken) {
        // 1. 카카오 API로 사용자 정보 요청
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user info from Kakao API.");
        }

        Map<String, Object> responseBody = response.getBody();

        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
        if(kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if(profile != null) {
                return profile.get("profile_image_url").toString();
            } else {
                return ""; //추후에 기본 프로필 이미지 링크 추가
            }
        }
        return null; //추후에 기본 프로필 이미지 링크 추가
    }
}
