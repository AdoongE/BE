package com.adoonge.seedzip.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.category.dto.request.AddCategoryRequest;
import com.adoonge.seedzip.category.repository.CategoryRepository;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional
	public Category createCategory(AddCategoryRequest request, Member member){
		return categoryRepository.save(request.toEntity(member));
	}
}
