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
import com.adoonge.seedzip.bookmark.dto.response.CategoryBookmarkResponse;
import com.adoonge.seedzip.bookmark.service.BookmarkService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
@Tag(name = "BookmarkController", description = "북마크 관련 API")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/category/{categoryId}")
	@Operation(summary = "카테고리 북마크 추가 API", description = "카테고리 북마크 추가 API입니다.")
	public ApiResponse<CategoryBookmarkResponse> addBookmark(
		@PathVariable Long categoryId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		CategoryBookmarkResponse categoryBookmarkResponse = bookmarkService.addCategoryBookmark(categoryId, member);

		return new ApiResponse<>(categoryBookmarkResponse);
	}

	@DeleteMapping("/category/{bookmarkId}")
	@Operation(summary = "카테고리 북마크 삭제 API", description = "카테고리 북마크 삭제 API입니다.")
	public ApiResponse<Void> deleteBookmark(
		@PathVariable Long bookmarkId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		bookmarkService.deleteCategoryBookmark(bookmarkId, member);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping("/category/bookmark")
	@Operation(summary = "카테고리 북마크 조회 API", description = "카테고리 북마크 조회 API입니다.")
	public ApiResponse<CategoryBookmarkResponse> getBookmarks(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();
		List<CategoryBookmarkResponse> bookmarks = bookmarkService.getCategoryBookmarks(member);

		return new ApiResponse<>(bookmarks);
	}

	@PostMapping("/seed/{seedId}")
	@Operation(summary = "씨드 북마크 추가 API", description = "씨드 북마크 추가 API입니다.")
	public ApiResponse<Void> addSeedBookmark(
			@PathVariable Long seedId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {

		Member member = customUserDetails.getMember();

		bookmarkService.addSeedBookmark(seedId, member);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}
}
