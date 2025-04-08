package com.adoonge.seedzip.seed.dto.response;
import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.seed.domain.SeedType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class SeedResponse {

    @Builder
    public record PageInfo(int page, int size, int totalPages, long totalElements, boolean isLast) {}

    @Builder
    public record SeedInfo(Long seedId, String seedName, List<Long> categoryId, List<String> categoryName,
                           SeedType seedType, String thumbnailImage, LocalDateTime updatedDt,
                           List<Long> tagId, List<String> tagName, int dDay) {}

    @Builder
    public record GetAllSeeds(String nickname, List<SeedInfo> seedInfoList, PageInfo pageInfo) {}

    @Builder
    public record SeedInfoSimple(Long seedId, String seedName){}
}
