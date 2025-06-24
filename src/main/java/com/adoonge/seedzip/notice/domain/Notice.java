package com.adoonge.seedzip.notice.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.notice.dto.request.NoticeUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notice extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String title;

    @Lob
    private String content;

    private Boolean isImportant;

    private Boolean isVisible;

    public void update(NoticeUpdateRequest request) {
        if (request.title() != null) this.title = request.title();
        if (request.content() != null) this.content = request.content();
        if (request.type() != null) this.type = request.type();
        if (request.isImportant() != null) this.isImportant = request.isImportant();
    }

    public void updateIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
