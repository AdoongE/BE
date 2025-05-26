package com.adoonge.seedzip.seed.controller;

import com.adoonge.seedzip.content.dto.response.ContentsAllResponse;
import com.adoonge.seedzip.seed.dto.request.SeedFilteringRequest;
import com.adoonge.seedzip.seed.dto.request.SeedUpdateRequest;
import com.adoonge.seedzip.seed.dto.response.SeedResponse.GetFilteredSeeds;
import java.util.List;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.dto.request.SeedRequest;
import com.adoonge.seedzip.seed.dto.response.SeedResponse;
import com.adoonge.seedzip.seed.service.SeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
                            schema = @Schema(implementation = SeedResponse.GetAllSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.GetAllSeeds> getAllSeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        SeedResponse.GetAllSeeds getAllSeeds = seedService.getAllSeeds(member, page, size, sortBy, isAsc, seedType);
        return new ApiResponse<>(getAllSeeds);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "카테고리 내 씨드 모아보기 API", description = "카테고리에 해당하는 콘텐츠를 조회하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.GetAllSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.GetAllSeeds> getCategorySeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @PathVariable("categoryId") Long categoryId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        SeedResponse.GetAllSeeds getAllSeeds = seedService.getCategorySeeds(member, page, size, sortBy, isAsc, seedType, categoryId);
        return new ApiResponse<>(getAllSeeds);
    }

    @GetMapping("/{seedId}")
    @Operation(summary = "씨드 상세 보기 API", description = "씨드 내용을 조회하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.SeedDetail.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.SeedDetail> getSeedDetail(@PathVariable("seedId") Long seedId,
                                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        return new ApiResponse<>(seedService.getSeedDetail(seedId));
    }

    @PostMapping
    @Operation(summary = "씨드 업로드 API", description = "씨드를 업로드하는 API입니다. 타입, 카테고리, 태그 2개 이상 필수입니다. "
        + "\n업로드 후, 업로드된 씨드의 ID를 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.SeedInfo.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.SeedInfoSimple> uploadSeed(
            @RequestBody @Valid SeedRequest seedRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();
        SeedResponse.SeedInfoSimple seedInfoApiResponse = seedService.uploadSeed(seedRequest, member);

        return new ApiResponse<>(seedInfoApiResponse);
    }

    @PostMapping(value = "/upload/{seedId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "씨드 업로드 및 수정 후 파일 업로드 API", description = "씨드를 생성 후 파일을 db 및 aws에 저장하는 API입니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorCode.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<?> uploadFiles(
        @PathVariable("seedId") Long seedId,
        @Parameter(description = "업로드할 파일 리스트", content = @Content(mediaType = "application/octet-stream"))
        @RequestParam(value = "file", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        seedService.uploadFiles(seedId, files);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    /**
     * 필터링 및 검색
     */
    @PostMapping("/filtering")
    @Operation(summary = "전체 씨드 필터링 및 검색 API", description = "전체 씨드를 필터링 및 검색하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.GetFilteredSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.GetFilteredSeeds> getAllSeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @RequestBody SeedFilteringRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        GetFilteredSeeds filteredSeeds = seedService.getFilteredSeeds(member, page, size, sortBy, isAsc, seedType,
                request);

        return new ApiResponse<>(filteredSeeds);
    }

    @PostMapping("/filtering/{categoryId}")
    @Operation(summary = "카테고리 내 필터링 및 검색 API", description = "카테고리 내 씨드를 필터링 및 검색하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeedResponse.GetFilteredSeeds.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.GetFilteredSeeds> getFilteredCategorySeeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sortBy, // latest or name
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String seedType,
            @PathVariable Long categoryId,
            @RequestBody SeedFilteringRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        GetFilteredSeeds filteredSeeds = seedService.getFilteredCategorySeeds(member, page, size, sortBy, isAsc, seedType,
                categoryId,request);

        return new ApiResponse<>(filteredSeeds);
    }

    @DeleteMapping("/{seedId}")
    @Operation(summary = "씨드 삭제 API", description = "씨드 삭제 API입니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SeedResponse.GetFilteredSeeds.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<Void> deleteSeed(@PathVariable("seedId") Long seedId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();
        seedService.deleteSeed(seedId, member);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PatchMapping(value = "/{seedId}")
    @Operation(summary = "씨드 수정 API", description = "씨드 내용을 수정하는 API입니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SeedResponse.SeedInfoSimple.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<SeedResponse.SeedInfoSimple> modifySeed(
        @PathVariable("seedId") Long seedId,
        @Parameter(description = "JSON 요청 데이터", content = @Content(mediaType = "application/json"))
        @RequestBody @Valid SeedUpdateRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        SeedResponse.SeedInfoSimple seedInfoSimple = seedService.updateSeed(request, seedId, member);

        return new ApiResponse<>(seedInfoSimple);
    }

}
