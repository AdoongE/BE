package com.adoonge.seedzip.member.dto.response;

import com.adoonge.seedzip.member.domain.Gender;
import com.adoonge.seedzip.member.domain.Member;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record MemberInformationResponse(
        String nickname,

        LocalDate birthday,

        Gender gender,

        String occupation,
        String field,

        Boolean consentToMarketingAndAds
) {
    public static MemberInformationResponse toDto(Member member) {
       return MemberInformationResponse.builder()
               .nickname(member.getNickname())
               .birthday(member.getBirthday())
               .gender(member.getGender())
               .occupation(member.getOccupation())
               .field(member.getField())
               .consentToMarketingAndAds(member.getConsentToMarketingAndAds())
               .build();
    }

}
