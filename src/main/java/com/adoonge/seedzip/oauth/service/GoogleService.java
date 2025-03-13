package com.adoonge.seedzip.oauth.service;

import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleService implements OAuthService{

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @Value("${GOOGLE_REDIRECT_URI}")
    private String googleRedirectUri;

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
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("redirect_uri", googleRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // POST 요청 실행
        String requestUrl = "https://oauth2.googleapis.com/token";
        return restTemplate.exchange(requestUrl, HttpMethod.POST, request, Map.class);    }

    @Override
    public ResponseEntity<Map> requestSocialUserInfo(String socialAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + socialAccessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);
        String requestUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

        return restTemplate.exchange(requestUrl, HttpMethod.GET, request, Map.class);    }

    @Override
    public String getSocialAccessToken(String socialCode) {
        ResponseEntity<Map> response = requestSocialUserAccessToken(socialCode);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            return responseBody != null ? (String) responseBody.get("access_token") : null;
        } else {
            throw SeedzipException.from(ErrorCode.OAUTH2_INVALID_CODE);
        }
    }

    @Override
    public String getLoginId(String accessToken) {
        // 1. Google API로 사용자 정보 요청
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user info from Google API.");
        }

        Map<String, Object> responseBody = response.getBody();

        return responseBody.get("sub").toString();
    }

    @Override
    public String getProfileImageUrl(String accessToken) {
        // 1. Google API로 사용자 정보 요청
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user info from Google API.");
        }

        Map<String, Object> responseBody = response.getBody();

        return responseBody.get("picture").toString();
    }

}
