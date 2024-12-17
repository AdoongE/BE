package com.adoonge.seedzip.filter.domain;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ColumnDefault("0")
	private Long filterNum;

	// JSON 형태로 저장 (ex. ["pdf", "image"])
	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private List<String> storageFormats;

	private LocalDate startDate;

	private LocalDate endDate;

	@Min(0)
	private Long dDayStart;

	@Min(0)
	private Long dDayEnd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Transient
	private final ObjectMapper objectMapper = new ObjectMapper();

	// storageFormats(JSON)를 List<String>으로 변환
	// public List<String> getStorageForamtList() {
	// 	try {
	// 		if(storageFormats == null){return null;}
	// 		return objectMapper.readValue((JsonParser)storageFormats, new TypeReference<List<String>>() {});
	// 	} catch (JsonProcessingException  e) {
	// 		throw new RuntimeException("Failed to parse storageFormats");
	// 	}
	// }


	@Builder
	public Filter(String name, List<String> storageFormats, LocalDate startDate, LocalDate endDate, Long dDayStart, Long dDayEnd, Member member, Long filterNum) {
		this.name = name;
		this.storageFormats = storageFormats;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dDayStart = dDayStart;
		this.dDayEnd = dDayEnd;
		this.member = member;
		this.filterNum = filterNum;
	}
}
