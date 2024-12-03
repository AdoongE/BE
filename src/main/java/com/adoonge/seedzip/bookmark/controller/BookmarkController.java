package com.adoonge.seedzip.bookmark.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.bookmark.dto.response.BookmarkResponse;
import com.adoonge.seedzip.bookmark.service.BookmarkService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "BookmarkController", description = "카테고리 북마크 관련 API")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("category/{categoryId}/bookmark")
	@Operation(summary = "북마크 추가 API", description = "북마크 추가 API입니다.")
	public ApiResponse<BookmarkResponse> addBookmark(
		@PathVariable Long categoryId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		BookmarkResponse bookmarkResponse = bookmarkService.addBookmark(categoryId, member);

		return new ApiResponse<>(bookmarkResponse);
	}

	@DeleteMapping("/bookmark/{bookmarkId}")
	@Operation(summary = "북마크 삭제 API", description = "북마크 삭제 API입니다.")
	public ApiResponse<Void> deleteBookmark(
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		bookmarkService.deleteBookmark(bookmarkId, member);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/bookmark")
	@Operation(summary = "북마크 조회 API", description = "북마크 조회 API입니다.")
	public ApiResponse<BookmarkResponse> getBookmarks(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		List<BookmarkResponse> bookmarks = bookmarkService.getBookmarks(member);

		return new ApiResponse<>(bookmarks);
	}
}
