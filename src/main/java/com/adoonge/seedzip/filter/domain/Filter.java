package com.adoonge.seedzip.filter.domain;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "filters")
@Getter
public class Filter extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long filterId;

	@Column(length = 20, nullable = false)
	private String name;

	// 이름이 null로 들어온 경우에 사용
	private Long filterNum;

	// JSON 형태로 저장 (ex. ["pdf", "image"])
	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private List<String> storageFormats;

	private LocalDate startDate;

	private LocalDate endDate;

	private Long fromDDay;	// D-Day 시작 범위

	private Long toDDay;	// D-Day 끝 범위

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	@OnDelete(action = OnDeleteAction.CASCADE)  // Member 삭제 시 Filter도 삭제
	private Member member;

	@OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FilterTag> filterTags;

	@Builder
	public Filter(String name, List<String> storageFormats, LocalDate startDate, LocalDate endDate, Long fromDDay, Long toDDay, Member member, Long filterNum) {
		this.name = name;
		this.storageFormats = storageFormats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fromDDay = fromDDay;
		this.toDDay = toDDay;
		this.member = member;
		this.filterNum = filterNum;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public Filter update(List<String> storageFormats, LocalDate startDate, LocalDate endDate, Long fromDDay, Long toDDay) {
		this.storageFormats = storageFormats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fromDDay = fromDDay;
		this.toDDay = toDDay;

		return this;
	}

}
