package com.adoonge.seedzip.notice.dto.response;

import com.adoonge.seedzip.notice.domain.Notice;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record NoticeDetailResponse(
        Long id,
        String type,
        String title,
        String content,
        boolean isImportant,
        boolean isNew,
        String createdAt
) {
    public static NoticeDetailResponse from(Notice notice) {
        return new NoticeDetailResponse(
                notice.getId(),
                notice.getType(),
                notice.getTitle(),
                notice.getContent(),
                notice.getIsImportant(),
                notice.getCreatedAt().isAfter(LocalDateTime.now().minusDays(7)),
                notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        );
    }
}
