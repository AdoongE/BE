package com.adoonge.seedzip.seed.dto.response;

import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.seed.domain.SeedType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SeedResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        int page;
        int size;
        int totalPages;
        long totalElements;
        boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class seedInfo{
        Long seedId;
        String seedName;
        List<Long> categoryId;
        List<String> categoryName;
        SeedType seedType;
        String thumbnailImage; // 없으면 null
        LocalDateTime updatedDt;
        List<Long> tagId;
        List<String> tagName;
        int dDay;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAllSeeds{
        String nickname;
        List<seedInfo> seedInfoList;
        PageInfo pageInfo;
    }
}
