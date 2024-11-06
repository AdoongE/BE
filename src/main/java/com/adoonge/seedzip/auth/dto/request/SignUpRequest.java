package com.adoonge.seedzip.auth.dto.request;

import com.adoonge.seedzip.auth.domain.SocialType;
import com.adoonge.seedzip.member.domain.Gender;
import com.adoonge.seedzip.member.domain.Member;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private SocialType socialType;
    private String accessToken;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
    private String occupation;
    private String field;
    private Boolean consentToTermsOfService; // 서비스 이용 약관 동의
    private Boolean consentToPersonalInformation; // 개인정보 수집 및 이용 동의
    private Boolean consentToMarketingAndAds; // 마케팅 활용 및 광고성 정보 수신 동의 여부

    public Member toEntity(String loginId, String encodedPassword, String profileImageUrl) {
        return Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .birthday(birthday)
                .gender(gender)
                .occupation(occupation)
                .field(field)
                .profileImageUrl(profileImageUrl)
                .consentToTermsOfService(consentToTermsOfService)
                .consentToPersonalInformation(consentToPersonalInformation)
                .consentToMarketingAndAds(consentToMarketingAndAds)
                .build();
    }
}

