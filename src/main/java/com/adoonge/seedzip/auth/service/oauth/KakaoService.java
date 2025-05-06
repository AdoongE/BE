package com.adoonge.seedzip.auth.service.oauth;

import com.adoonge.seedzip.auth.dto.response.oauth.kakao.KakaoTokenResponse;
import com.adoonge.seedzip.auth.dto.response.oauth.kakao.KakaoUserInfoResponse;
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
public class KakaoService implements OAuthService {

    private final WebClient webClient;
    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    @Override
    public String getAccessToken(String code) {
        String requestBody =
                "grant_type=authorization_code"
                        + "&client_id="
                        + kakaoApiKey
                        + "&redirect_uri="
                        + kakaoRedirectUri
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
                                return response.bodyToMono(KakaoTokenResponse.class);
                            } else {
                                return response
                                        .bodyToMono(String.class)
                                        .flatMap(
                                                errorBody ->
                                                        Mono.error(
                                                                SeedzipException.from(ErrorCode.GET_KAKAO_ACCESS_TOKEN_FAILED)));
                            }
                        })
                .map(KakaoTokenResponse::accessToken)
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
                        clientResponse -> Mono.error(SeedzipException.from(ErrorCode.GET_KAKAO_UNIQUE_ID_FAILED)))
                .bodyToMono(KakaoUserInfoResponse.class)
                .map(response -> String.valueOf(response.id()))
                .block();
    }
}
