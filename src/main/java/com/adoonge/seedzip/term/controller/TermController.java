package com.adoonge.seedzip.term.controller;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.term.dto.response.TermResponse;
import com.adoonge.seedzip.term.entity.TermType;
import com.adoonge.seedzip.term.service.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/term")
@RequiredArgsConstructor
@Tag(name = "TermController", description = "약관 관련 API")
public class TermController {

    private final TermService termService;

    @GetMapping("/{type}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TermResponse.class)))
    })
    @Operation(summary = "약관 내용 조회 API", description = "약관 내용 조회 API입니다.")
    public ApiResponse<TermResponse> getTerm(@PathVariable TermType type) {
        return new ApiResponse<>(termService.getTermByType(type));
    }

}
