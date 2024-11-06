package com.adoonge.seedzip.category.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoonge.seedzip.auth.util.CustomUserDetails;
import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.category.service.CategoryService;
import com.adoonge.seedzip.global.dto.response.ApiResponse;
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
	public ApiResponse<Category> createCategory(@RequestBody AddCategoryRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		Member member = customUserDetails.getMember();
		Category createdCategory = categoryService.createCategory(request, member);
		return new ApiResponse<>(createdCategory);
	}
}
