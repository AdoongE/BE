package com.adoonge.seedzip.member.dto.request;

import com.adoonge.seedzip.member.domain.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public record UpdateMemberRequest(

        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{1,10}$", message = "닉네임은 한글, 영문, 숫자, 공백 포함 10자 이내로 작성해야 합니다.")
        String nickname,

        @NotNull(message = "생년월일은 필수입니다.")
        @PastOrPresent(message = "생년월일은 과거 또는 오늘 날짜여야 합니다.")
        LocalDate birthday,

        @NotNull(message = "성별은 필수입니다.")
        Gender gender,

        String occupation,
        String field,

        Boolean consentToMarketingAndAds
) {}
