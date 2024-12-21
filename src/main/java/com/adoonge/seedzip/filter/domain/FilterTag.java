package com.adoonge.seedzip.filter.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.tag.domain.Tag;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@Entity
@Table(name = "filter_tags")
@Getter
public class FilterTag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long filterTagId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Tag tag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "filter_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Filter filter;

	@Builder
	public FilterTag(Tag tag, Filter filter) {
		this.tag = tag;
		this.filter = filter;
	}
}
