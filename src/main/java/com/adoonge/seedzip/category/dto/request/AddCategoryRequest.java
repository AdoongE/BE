package com.adoonge.seedzip.category.dto.request;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.domain.Visibility;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {

	private String name;
	private Visibility visibility;

	public Category toEntity(Member member){
		return Category.builder()
			.name(name)
			.visibility(visibility)
			.member(member)
			.build();
	}

}
