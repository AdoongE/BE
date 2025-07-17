package com.adoonge.seedzip.seed.dto.response;
import com.adoonge.seedzip.seed.domain.SeedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public class SeedResponse {

    @Builder
    public record PageInfo(int page, int size, int totalPages, long totalElements, boolean isLast) {}

    @Builder
    public record SeedInfo(Long id, String name, List<Long> categoryIds, List<String> categoryNames,
                           SeedType type, String thumbnailImage, LocalDateTime updatedDt,
                           List<Long> tagIds, List<String> tagNames, int dDay, Boolean isSaved) {}

    @Builder
    public record GetAllSeeds(String nickname, List<SeedInfo> seedInfoList, PageInfo pageInfo) {}

    @Builder
    public record SeedInfoSimple(Long id, String name){}

    @Builder
    public record SeedDetail(Long id,
            SeedType type,
            String name,
            String link,
            List<String> fileLinks,
            List<String> fileTitles,
            Long thumbnailImage,
            List<String> categoryNames,
            List<String> tagNames,
            LocalDate dDay,
            String detail){}

    @Builder
    public record SeedInfoWithSeedDetail(
            Long id, String name, List<Long> categoryIds, List<String> categoryNames,
            SeedType type, String thumbnailImage, LocalDateTime updatedDt,
            List<Long> tagIds, List<String> tagNames, int dDay, String detail, Boolean isSaved
    ){}

    @Builder
    public record GetFilteredSeeds(String nickname, List<SeedInfoWithSeedDetail> seedInfoList, PageInfo pageInfo){}
}
