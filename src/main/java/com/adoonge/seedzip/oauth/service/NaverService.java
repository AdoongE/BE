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
public class NaverService implements OAuthService{

    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;

    @Value("${NAVER_CLIENT_SECRET}")
    private String naverClientSecret;

    @Value("${NAVER_REDIRECT_URI}")
    private String naverRedirectUri;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<Map> requestSocialUserAccessToken(String code) {
        String requestUrl = "https://nid.naver.com/oauth2.0/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("redirect_uri", naverRedirectUri);
        body.add("state", "seedzip");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.exchange(requestUrl, HttpMethod.POST, request, Map.class);
    }

    @Override
    public ResponseEntity<Map> requestSocialUserInfo(String socialAccessToken) {
        String requestUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + socialAccessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<>(headers);
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
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Naver API에서 사용자 정보를 가져오지 못했습니다.");
        }

        Map<String, Object> responseBody = response.getBody();
        if (!responseBody.containsKey("response")) {
            throw new RuntimeException("Naver API 응답에 사용자 정보가 없습니다.");
        }

        Map<String, Object> userInfo = (Map<String, Object>) responseBody.get("response");

        return userInfo.get("id").toString();
    }

    @Override
    public String getProfileImageUrl(String accessToken) {
        ResponseEntity<Map> response = requestSocialUserInfo(accessToken);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Naver API에서 사용자 정보를 가져오지 못했습니다.");
        }

        Map<String, Object> responseBody = response.getBody();
        if (!responseBody.containsKey("response")) {
            throw new RuntimeException("Naver API 응답에 사용자 정보가 없습니다.");
        }

        Map<String, Object> userInfo = (Map<String, Object>) responseBody.get("response");
        if(userInfo != null){
            return userInfo.get("profile_image").toString();
        }
        return null; // 추후에 기본 프로필 이미지 링크 추가
    }

}
