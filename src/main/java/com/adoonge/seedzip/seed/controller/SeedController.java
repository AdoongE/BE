package com.adoonge.seedzip.seed.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.dto.response.SeedResponse;
import com.adoonge.seedzip.seed.service.SeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/seed")
@RequiredArgsConstructor
@Tag(name = "SeedController", description = "씨드(콘텐츠) 관련 API")
public class SeedController {

    private final SeedService seedService;

    @GetMapping
    @Operation(summary = "전체 씨드 모아보기 API", description = "전체 씨드를 홈화면에서 조회하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.getAllSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.getAllSeeds> getAllSeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 빈 값 처리
        if (sortBy.isBlank()) sortBy = "latest";

        Member member = customUserDetails.getMember();

        SeedResponse.getAllSeeds getAllSeeds = seedService.getAllSeeds(member, page, size, sortBy, isAsc, seedType);
        return new ApiResponse<>(getAllSeeds);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 내 씨드 모아보기 API", description = "카테고리에 해당하는 콘텐츠를 조회하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.getAllSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.getAllSeeds> getCategorySeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @PathVariable("categoryId") Long categoryId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 빈 값 처리
        if (sortBy.isBlank()) sortBy = "latest";

        Member member = customUserDetails.getMember();

        SeedResponse.getAllSeeds getAllSeeds = seedService.getCategorySeeds(member, page, size, sortBy, isAsc, seedType, categoryId);
        return new ApiResponse<>(getAllSeeds);
    }

}
