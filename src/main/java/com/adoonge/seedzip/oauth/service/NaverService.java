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
public class NaverService implements OAuthService{

    @Value("${NAVER_CLIENT_ID}")
    private String clientId;

    @Value("${NAVER_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${NAVER_REDIRECT_URI}")
    private String redirectUri;

    @Override
    public String getAccessToken(String code) {
        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader Object
        HttpHeaders headers = new HttpHeaders();

        // HttpBody Object
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", "seedzip");

        // http body params 와 http headers 를 가진 엔티티
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        // reqUrl로 Http 요청, POST 방식
        ResponseEntity<String> response = restTemplate.exchange(reqUrl,
                HttpMethod.POST,
                naverTokenRequest,
                String.class);

        String responseBody = response.getBody();
        JsonObject asJsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        return asJsonObject.get("access_token").getAsString();
    }

    @Override
    public String getLoginId(String accessToken) {
        Map<String, Object> naverAccount = (Map<String, Object>) getUserAttributesByToken(accessToken).get("response");
        return naverAccount.get("id").toString();
    }

    @Override
    public String getProfileImageUrl(String accessToken) {
        Map<String, Object> naverAccount = (Map<String, Object>) getUserAttributesByToken(accessToken).get("response");
        if(naverAccount != null){
            return naverAccount.get("profile_image").toString();
        }
        return null; // 추후에 기본 프로필 이미지 링크 추가
    }

    private Map<String, Object> getUserAttributesByToken(String accessToken){
        return WebClient.create()
                .get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
