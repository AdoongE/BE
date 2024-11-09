package com.adoonge.seedzip.bookmark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.bookmark.dto.response.BookmarkResponse;
import com.adoonge.seedzip.bookmark.service.BookmarkService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "BookmarkController", description = "카테고리 북마크 관련 API")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/{id}/bookmark")
	@Operation(summary = "북마크 추가 API", description = "북마크 추가 API입니다.")
	public ApiResponse<BookmarkResponse> addBookmark(@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(id, member);

		return new ApiResponse<>(bookmarkResponse);
	}
}
