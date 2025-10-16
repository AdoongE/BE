package com.adoonge.seedzip.seed.dto.request;

import java.time.LocalDate;
import com.adoonge.seedzip.seed.domain.SeedType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 앱 전용 씨드 수정 요청 DTO
 * - 파일(이미지, 링크)은 수정 불가
 * - 제목/내용/태그/카테고리 등만 수정 가능
 */
public record SeedUpdateRequestForApp(
        @NotNull SeedType seedType,
        String seedName,                                  // 제목 (없으면 null)
        @NotNull String[] categoryName,                   // 카테고리 (없으면 null)
        @NotNull @Size(min = 2, message = "태그는 최소 2개 이상 입력해야 합니다.") String[] tagName,
        LocalDate dDay,                                   // 없으면 null
        String seedDetail                                 // 없으면 null
) {}
