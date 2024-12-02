package com.adoonge.seedzip.category.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequest(
	@NotNull Long categoryId,
	@NotNull String name
){

}
