package com.adoonge.seedzip.faq.controller;

import com.adoonge.seedzip.faq.dto.response.FaqResponse;
import com.adoonge.seedzip.faq.service.FaqService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
