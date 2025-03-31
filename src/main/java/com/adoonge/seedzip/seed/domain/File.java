package com.adoonge.seedzip.seed.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.global.entity.BaseEntity;

import jakarta.persistence.Column;
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
@Builder
@Entity
@Table(name = "files")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class File extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long id;

	@Column(nullable = false, length = 1000)
	private String link;	// 이미지, pdf는 aws s3 링크

	private String fileName;

	private Boolean isThumbnail;	// 링크, pdf는 null

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seed_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Seed seed;
}
