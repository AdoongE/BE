package com.adoonge.seedzip.notice.dto.request;

import com.adoonge.seedzip.notice.domain.Notice;

public record NoticeCreateRequest(String type, String title, String content, Boolean isImportant) {
    public static Notice toEntity(NoticeCreateRequest request) {
        return Notice.builder()
                .type(request.type())
                .title(request.title())
                .content(request.content())
                .isImportant(request.isImportant())
                .isVisible(true)
                .build();
    }
}
