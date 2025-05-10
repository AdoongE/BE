package com.adoonge.seedzip.notice.dto.request;

public record NoticeUpdateRequest(
        String type,
        String title,
        String content,
        Boolean isImportant
) {
}
