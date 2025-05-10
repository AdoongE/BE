package com.adoonge.seedzip.member.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.dto.request.UpdateMemberRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId; //유저를 고유하게 식별할 수 있는 값

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String nickname;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String occupation;

    private String field;

    private String profileImageUrl;

    private Boolean consentToTermsOfService; // 서비스 이용 약관 동의

    private Boolean consentToPersonalInformation; // 개인정보 수집 및 이용 동의

    private Boolean consentToMarketingAndAds; // 마케팅 활용 및 광고성 정보 수신 동의 여부

    public void update(UpdateMemberRequest request) {
        this.nickname = request.nickname();
        this.birthday = request.birthday();
        this.gender = request.gender();
        this.occupation = request.occupation();
        this.field = request.field();
        this.consentToMarketingAndAds = request.consentToMarketingAndAds();
    }
}
