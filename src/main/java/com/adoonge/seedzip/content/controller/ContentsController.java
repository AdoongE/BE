package com.adoonge.seedzip.content.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.content.dto.request.ContentsRequest;
import com.adoonge.seedzip.content.dto.response.ContentsResponse;
import com.adoonge.seedzip.content.service.ContentsService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
@Tag(name = "ContentController", description = "콘텐츠 관련 API")
public class ContentsController {

    @Autowired
    private final ContentsService contentsService;

    @PostMapping(value ="/doc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "콘텐츠(문서) 생성 API", description = "콘텐츠(문서) 생성 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 업로드됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContentsResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ApiResponse<ContentsResponse> createDocContents(
            @Parameter(description = "업로드할 파일 리스트", content = @Content(mediaType = "application/octet-stream"))
            @RequestParam("file") List<MultipartFile> files,
            @Parameter(description = "JSON 요청 데이터", content = @Content(mediaType = "application/json"))
            @RequestPart("request") ContentsRequest.docContentsRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();
        ContentsResponse createdDocContents = contentsService.createDocContents(request, files, member);

        return new ApiResponse<>(createdDocContents);
    }
}
