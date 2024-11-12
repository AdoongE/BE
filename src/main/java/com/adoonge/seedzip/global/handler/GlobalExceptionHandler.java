package com.adoonge.seedzip.global.handler;

import com.adoonge.seedzip.global.dto.response.ApiResponse;
import com.adoonge.seedzip.global.exception.ErrorCode;
import com.adoonge.seedzip.global.exception.SeedzipException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ApiResponse<Void> validHandler(MethodArgumentNotValidException exception) {
//        Map<String, String> errors = new HashMap<>();
//
//        //첫번째 발생하는 검증 오류 메시지만 가져오기
//        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//
//        ErrorCode errorCode = ErrorCode.fromMessage(message);
//
//        return new ApiResponse<>(errorCode);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        // 첫 번째 발생한 검증 오류 메시지를 가져오기
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 오류 메시지로부터 ErrorCode를 찾기
        ErrorCode errorCode = ErrorCode.fromMessage(message);

        // ApiResponse 생성
        ApiResponse<Void> apiResponse = new ApiResponse<>(errorCode);

        // ResponseEntity로 ApiResponse를 반환
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


//    //@Valid 또는 @Validated 어노테이션을 사용하여
//    //DTO 객체에 설정한 유효성 검사 조건을 만족하지 못했을 때 발생하는 예외처리
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatusCode status,
//            WebRequest request) {
//
//        BindingResult bindingResult = ex.getBindingResult();
//        StringBuilder builder = new StringBuilder();
//        for (FieldError fieldError : bindingResult.getFieldErrors()) {
//            builder.append("[");
//            builder.append(fieldError.getField());
//            builder.append("]의 값이 잘못됐습니다. ");
//            builder.append("입력된 값: [");
//            builder.append(fieldError.getRejectedValue());
//            builder.append("]");
//        }
//
//        ApiResponse<String> apiResponse = new ApiResponse<>(builder.toString());
//        log.warn(ex.getMessage(), ex);
//
//        return new ResponseEntity<>(apiResponse, headers, status);
//    }
}
