package com.adoonge.seedzip.notice.controller;

import com.adoonge.seedzip.auth.dto.response.LoginResponse;
import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.notice.dto.request.NoticeCreateRequest;
import com.adoonge.seedzip.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
@Tag(name = "NoticeController", description = "공지사항 관련 API")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @Operation(summary = "공지사항 등록 API", description = "공지사항 등록 API입니다. 관리자만 가능합니다.")
    public ApiResponse<Void> login(@RequestBody NoticeCreateRequest request,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        noticeService.createNotice(request, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }
}
