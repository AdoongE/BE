package com.adoonge.seedzip.notice.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.notice.dto.request.NoticeCreateRequest;
import com.adoonge.seedzip.notice.dto.request.NoticeUpdateRequest;
import com.adoonge.seedzip.notice.dto.response.NoticeDetailResponse;
import com.adoonge.seedzip.notice.dto.response.NoticeResponse;
import com.adoonge.seedzip.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
@Tag(name = "NoticeController", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @Operation(summary = "공지사항 등록 API", description = "공지사항 등록 API입니다. 관리자만 가능합니다.")
    public ApiResponse<Void> createNotice(@RequestBody NoticeCreateRequest request,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        noticeService.createNotice(request, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @GetMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NoticeResponse.class)))
    })
    @Operation(summary = "공지사항 목록 조회 API", description = "공지사항 목록 조회 API입니다.")
    public ApiResponse<NoticeResponse> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ApiResponse<>(noticeService.getNotices(page, size));
    }

    @GetMapping("{id}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NoticeDetailResponse.class)))
    })
    @Operation(summary = "공지사항 상세 조회 API", description = "공지사항 상세 조회 API입니다.")
    public ApiResponse<NoticeDetailResponse> getNoticeDetail(@PathVariable Long id) {
        return new ApiResponse<>(noticeService.getNoticeDetail(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "공지사항 수정 API", description = "공지사항 수정 API입니다.")
    public ApiResponse<Void> updateNotice(@PathVariable Long id,
                                          @RequestBody NoticeUpdateRequest request,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        noticeService.updateNotice(id, request, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "공지사항 삭제 API", description = "공지사항 삭제 API입니다.")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        noticeService.deleteNotice(id, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

}
