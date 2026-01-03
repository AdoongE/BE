package com.adoonge.seedzip.auth.dto.response;

import com.adoonge.seedzip.auth.domain.SocialType;

import lombok.Builder;

@Builder
public record BasicLoginResponse(String result, SocialType socialType) {
}
