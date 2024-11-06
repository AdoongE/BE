package com.adoonge.seedzip.auth.dto.response;

import com.adoonge.seedzip.auth.domain.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String result;
    private SocialType socialType;
}
