package com.adoonge.seedzip.auth.dto.request;

import java.time.LocalDate;

import com.adoonge.seedzip.member.domain.Gender;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.domain.Role;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicSignUpRequest{

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "유효한 이메일 형식이어야 합니다.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", message = "비밀번호는 영문자와 숫자를 포함한 8~20자여야 합니다.")
	private String password;

	@NotBlank(message = "닉네임은 필수입니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9 ]{1,10}$", message = "닉네임은 한글, 영문, 숫자, 공백 포함 10자 이내로 작성해야 합니다.")
	private String nickname;

	@NotNull(message = "생년월일은 필수입니다.")
	@PastOrPresent(message = "생년월일은 과거 또는 오늘 날짜여야 합니다.")
	private LocalDate birthday;

	private Gender gender;
	private String occupation;
	private String field;

	// 필수 - 서비스 이용 약관 동의
	@AssertTrue(message = "서비스 이용 약관에 동의해야 합니다.")
	private Boolean consentToTermsOfService;

	// 필수 - 개인정보 수집 및 이용 동의
	@AssertTrue(message = "개인정보 수집 및 이용에 동의해야 합니다.")
	private Boolean consentToPersonalInformation;

	private Boolean consentToMarketingAndAds; // 마케팅 활용 및 광고성 정보 수신 동의 여부

	public Member toEntity(String email, String encodedPassword, String profileImageUrl) {
		return Member.builder()
			.loginId(email)
			.password(encodedPassword)
			.nickname(nickname)
			.birthday(birthday)
			.gender(gender)
			.occupation(occupation)
			.field(field)
			.role(Role.USER)
			.profileImageUrl(profileImageUrl)
			.consentToTermsOfService(consentToTermsOfService)
			.consentToPersonalInformation(consentToPersonalInformation)
			.consentToMarketingAndAds(consentToMarketingAndAds)
			.build();
	}
}