package com.adoonge.seedzip.global.handler;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.SeedzipException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";

    // INFO 출력 예시
    /*
        [🔵INFO] - (POST /admin/info)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.BadRequestException: 테스트용 에러입니다.
     */

    // WARN 출력 예시
    /*
        [🟠WARN] - (POST /admin/warn)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.InternalServerException: 테스트용 에러입니다.
          at com.festago.admin.presentation.AdminController.getWarn(AdminController.java:129)
     */

    // ERROR 출력 예시
    /*
        [🔴ERROR] - (POST /admin/error)
        (id: 1, role: MEMBER)
         java.lang.IllegalArgumentException: 테스트용 에러입니다.
          at com.festago.admin.presentation.AdminController.getError(AdminController.java:129)
     */

    @ExceptionHandler(SeedzipException.class)
    public ApiResponse<Void> handle(SeedzipException exception, HttpServletRequest request) {
        logInfo(exception, request);

        return new ApiResponse<>(exception);
    }

    private void logInfo(SeedzipException exception, HttpServletRequest request) {
        log.info(LOG_FORMAT_INFO, request.getMethod(), request.getRequestURI(), exception.getClass().getName(), exception.getMessage());
    }

    //@Valid 또는 @Validated 어노테이션을 사용하여
    //DTO 객체에 설정한 유효성 검사 조건을 만족하지 못했을 때 발생하는 예외처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("]의 값이 잘못됐습니다. ");
            builder.append("입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        ApiResponse<String> apiResponse = new ApiResponse<>(builder.toString());
        log.warn(ex.getMessage(), ex);

        return new ResponseEntity<>(apiResponse, headers, status);
    }
}
