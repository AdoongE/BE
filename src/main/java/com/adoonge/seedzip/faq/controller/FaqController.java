package com.adoonge.seedzip.faq.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.faq.dto.request.FaqCreateRequest;
import com.adoonge.seedzip.faq.dto.request.FaqUpdateRequest;
import com.adoonge.seedzip.faq.dto.response.FaqResponse;
import com.adoonge.seedzip.faq.service.FaqService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
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
@RequestMapping("/api/v1/faq")
@RequiredArgsConstructor
@Tag(name = "FaqController", description = "FAQ 관련 API")
public class FaqController {

    private final FaqService faqService;

    @GetMapping
    @Operation(summary = "FAQ 조회 API", description = "FAQ 조회 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FaqResponse.class)))
    })
    public ApiResponse<FaqResponse> getFaqs(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return new ApiResponse<>(faqService.getFaqs(page, size));
    }

    @PostMapping
    @Operation(summary = "FAQ 등록 API", description = "FAQ 등록 API입니다.")
    public ApiResponse<Void> createFaq(@RequestBody FaqCreateRequest request,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        faqService.createFaq(request, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "FAQ 수정 API", description = "FAQ 수정 API입니다.")
    public ApiResponse<Void> updateFaq(
            @PathVariable Long id,
            @RequestBody FaqUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        faqService.updateFaq(id, request, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "FAQ 삭제 API", description = "FAQ 삭제 API입니다.")
    public ApiResponse<Void> deleteFaq(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        faqService.deleteFaq(id, customUserDetails.getMember());
        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

}
