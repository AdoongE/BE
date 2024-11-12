package com.adoonge.seedzip.category.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column(length = 50)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Visibility visibility;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@PrePersist
	public void setDefaultValues() {
		if (this.name == null) {
			this.name = "새 카테고리";
		}
	}

	public void updateCategory(String name) {
		this.name = name;
	}
}
