package com.adoonge.seedzip.global;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Server Health", description = "서버가 정상 작동하는지 확인")
public class HealthCheck {
    @GetMapping
    @Operation(summary = "헬스 체크 API", description = "서버가 정상 작동하는지 확인합니다.")
    public String check() { return "OK";}

}
