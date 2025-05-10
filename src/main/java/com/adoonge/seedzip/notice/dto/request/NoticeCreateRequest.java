package com.adoonge.seedzip.notice.dto.request;

import com.adoonge.seedzip.notice.domain.Notice;

public record NoticeCreateRequest(String type, String title, String content, Boolean isImportant) {
    public Notice toEntity() {
        return Notice.builder()
                .type(this.type)
                .title(this.title)
                .content(this.content)
                .isImportant(this.isImportant)
                .isVisible(true)
                .build();
    }
}
