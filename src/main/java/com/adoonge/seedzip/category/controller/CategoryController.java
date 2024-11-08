package com.adoonge.seedzip.category.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.category.dto.request.UpdateCategoryRequest;
import com.adoonge.seedzip.category.dto.response.CategoryResponse;
import com.adoonge.seedzip.category.service.CategoryService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "CategoryController", description = "카테고리 관련 API")
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping
	@Operation(summary = "카테고리 생성 API", description = "카테고리 생성 API입니다.")
	public ApiResponse<CategoryResponse> createCategory(@RequestBody AddCategoryRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		CategoryResponse createdCategory = categoryService.createCategory(request, member);

		return new ApiResponse<>(createdCategory);
	}

	@GetMapping
	@Operation(summary = "카테고리 조회 API", description = "카테고리 조회 API입니다.")
	public ApiResponse<CategoryResponse> getCategories(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		List<CategoryResponse> categories = categoryService.getCategories(customUserDetails.getMember());

		return new ApiResponse<>(categories);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "카테고리 수정 API", description = "카테고리 수정 API입니다.")
	public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,
		@RequestBody UpdateCategoryRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		CategoryResponse updatedCategory = categoryService.updateCategory(id, request, member);

		return new ApiResponse<>(updatedCategory);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "카테고리 삭제 API", description = "카테고리 삭제 API입니다.")
	public ApiResponse<Void> deleteCategory(@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		categoryService.deleteCategory(id, member);

		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

}
