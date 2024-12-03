package com.adoonge.seedzip.bookmark.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookmarkId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder
	public Bookmark(Category category, Member member) {
		this.category = category;
		this.member = member;
	}

	public static Bookmark of(Category category, Member member) {
		return Bookmark.builder()
			.category(category)
			.member(member)
			.build();
	}
}