package com.adoonge.seedzip.oauth.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoService implements OAuthService {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;
    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    @Override
    public String getLoginId(String accessToken) {
        return getUserAttributesByToken(accessToken).get("id").toString();
    }

    @Override
    public String getProfileImageUrl(String accessToken) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) getUserAttributesByToken(accessToken).get("kakao_account");
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

    @Override
    public String getAccessToken(String code) {

        String reqUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // http 바디 params 와 http 헤더 headers 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // reqUrl 로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(reqUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        String responseBody = response.getBody();
        JsonObject asJsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        return asJsonObject.get("access_token").getAsString();
    }

    private Map<String, Object> getUserAttributesByToken(String accessCode){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessCode))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
