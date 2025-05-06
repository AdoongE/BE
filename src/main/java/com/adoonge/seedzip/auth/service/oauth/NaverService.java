package com.adoonge.seedzip.auth.service.oauth;

import com.adoonge.seedzip.auth.dto.response.oauth.naver.NaverTokenResponse;
import com.adoonge.seedzip.auth.dto.response.oauth.naver.NaverUserInfoResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NaverService implements OAuthService {

    private final WebClient webClient;
    private static final String TOKEN_REQUEST_URI = "https://nid.naver.com/oauth2.0/token";
    private static final String USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";

    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;

    @Value("${NAVER_CLIENT_SECRET}")
    private String naverClientSecret;

    @Value("${NAVER_REDIRECT_URI}")
    private String naverRedirectUri;

    @Override
    public String getAccessToken(String code) {
        String requestBody =
                "grant_type=authorization_code"
                        + "&client_id="
                        + naverClientId
                        + "&client_secret="
                        + naverClientSecret
                        + "&redirect_uri="
                        + naverRedirectUri
                        + "&code="
                        + code;

        return webClient
                .post()
                .uri(TOKEN_REQUEST_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(requestBody)
                .exchangeToMono(
                        response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(NaverTokenResponse.class);
                            } else {
                                return response
                                        .bodyToMono(String.class)
                                        .flatMap(
                                                errorBody ->
                                                        Mono.error(
                                                                SeedzipException.from(ErrorCode.GET_NAVER_ACCESS_TOKEN_FAILED)));
                            }
                        })
                .map(NaverTokenResponse::accessToken)
                .block();
    }

    @Override
    public String getLoginId(String accessToken) {
        return webClient
                .get()
                .uri(USER_INFO_URI)
                .header("Authorization", "Bearer " + accessToken.trim())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        clientResponse -> Mono.error(SeedzipException.from(ErrorCode.GET_NAVER_UNIQUE_ID_FAILED)))
                .bodyToMono(NaverUserInfoResponse.class)
                .map(response -> String.valueOf(response.naverUserInfo().id()))
                .block();
    }
}
