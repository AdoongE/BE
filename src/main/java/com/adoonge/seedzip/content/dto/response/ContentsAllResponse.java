package com.adoonge.seedzip.content.dto.response;

import com.adoonge.seedzip.content.domain.ContentsDataType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class ContentsAllResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contentsInfo{
        Long contentId;
        String contentName;
        List<Long> categoryId;
        List<String> categoryName;
        ContentsDataType contentDateType;
        String thumbnailImage; // 없으면 null
        LocalDateTime updatedDt;
        List<Long> tagId;
        List<String> tagName;
        String dDay;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAllContents{
        String nickname;
        List<contentsInfo> contentsInfoList;
    }
}
