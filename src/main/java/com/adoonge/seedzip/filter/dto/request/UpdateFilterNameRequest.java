package com.adoonge.seedzip.filter.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFilterNameRequest(
	@NotBlank(message = "필터 이름은 필수입니다.")
	@Size(max = 15, message = "필터 이름은 15자 이하여야 합니다.")
	String name
	) {
}
