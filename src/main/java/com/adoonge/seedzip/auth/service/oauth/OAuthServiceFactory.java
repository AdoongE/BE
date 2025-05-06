package com.adoonge.seedzip.auth.service.oauth;

import com.adoonge.seedzip.auth.domain.SocialType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service // 팩토리 패턴 사용해서 SocialType에 맞는 serivce 리턴
public class OAuthServiceFactory {

    private final Map<SocialType, OAuthService> oauthServices;

    public OAuthServiceFactory(List<OAuthService> oauthServiceList) {
        this.oauthServices = oauthServiceList.stream()
                .collect(Collectors.toMap(
                        service -> getSocialType(service),
                        service -> service
                ));
    }

    private SocialType getSocialType(OAuthService service) {
        if (service instanceof KakaoService) {
            return SocialType.KAKAO;
        }
        else if(service instanceof NaverService) {
            return SocialType.NAVER;
        }
        else if(service instanceof GoogleService) {
            return SocialType.GOOGLE;
        }
        else if (service instanceof BasicService) {
            return SocialType.BASIC;
        }
        throw new IllegalArgumentException("Unknown OAuthService type: " + service.getClass());
    }

    public OAuthService getOAuthService(SocialType socialType) {
        return oauthServices.get(socialType);
    }
}

