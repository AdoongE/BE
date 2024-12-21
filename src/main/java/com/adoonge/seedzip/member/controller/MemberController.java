package com.adoonge.seedzip.member.controller;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.member.dto.request.UpdateMemberRequest;
import com.adoonge.seedzip.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "사용자 개인 정보 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping
    @Operation(summary = "사용자 개인 정보 수정 API", description = "사용자 개인 정보를 수정 API입니다.")
    public ApiResponse<Void> updateMember(@RequestBody @Valid UpdateMemberRequest request
            , @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        memberService.update(member, request);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

    @DeleteMapping
    @Operation(summary = "사용자 개인 정보 삭제(탈퇴) API", description = "사용자 개인 정보를 삭제(탈퇴) API입니다.")
    public ApiResponse<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Member member = customUserDetails.getMember();

        memberService.delete(member);

        return new ApiResponse<>(ErrorCode.REQUEST_OK);
    }

}
