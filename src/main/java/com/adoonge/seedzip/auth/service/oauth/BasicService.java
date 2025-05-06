package com.adoonge.seedzip.auth.service.oauth;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicService implements OAuthService{

    @Override
    public String getAccessToken(String code) {
        return code;
    }

    @Override
    public String getLoginId(String loginId) {
        return loginId;
    }

}
