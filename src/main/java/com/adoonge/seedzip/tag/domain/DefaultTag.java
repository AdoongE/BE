package com.adoonge.seedzip.tag.domain;

import com.adoonge.seedzip.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("DefaultTag")
public class DefaultTag extends Tag{

	@Builder
	public DefaultTag(String name) {
		super(name); // 부모 클래스의 생성자 호출
	}
}
