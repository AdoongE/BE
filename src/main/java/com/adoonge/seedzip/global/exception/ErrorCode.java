package com.adoonge.seedzip.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // user
    MEMBER_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "로그인하지 않은 사용자입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보가 존재하지 않습니다"),
    DUPLICATE_MEMBER_LOGIN_ID(HttpStatus.CONFLICT, "중복된 로그인 아이디입니다"),
    DUPLICATE_MEMBER_PHONE_NUMBER(HttpStatus.CONFLICT, "중복된 전화번호입니다"),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 이미지를 찾을 수 없습니다"),
    MEMBER_NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자가 아닙니다"),

    // auth
    MEMBER_JOIN_REQUIRED(HttpStatus.MULTIPLE_CHOICES, "회원가입이 필요합니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 로그인 토큰입니다."),
    NOT_BEARER_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "Bearer 타입의 토큰이 아닙니다."),
    NEED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    INCORRECT_PASSWORD_OR_ACCOUNT(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸거나, 해당 계정이 없습니다."),
    ACCOUNT_USERNAME_EXIST(HttpStatus.UNAUTHORIZED, "해당 계정이 존재합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "존재하지 않은 리프래쉬 토큰으로 재발급 요청을 했습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프래쉬 토큰입니다."),
    OAUTH2_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth2 제공자 서버에 문제가 발생했습니다."),
    OAUTH2_INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 OAuth2 에러가 발생했습니다."),
    OPEN_ID_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OpenID 제공자 서버에 문제가 발생했습니다."),

    // file
    EMPTY_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일이 비어있습니다."),
    UNSUPPORTED_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 파일 확장자입니다."),
    IMAGE_STORE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다."),
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 파일 크기가 너무 큽니다."),
    S3_UPLOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에 파일을 업로드하는 중 에러가 발생했습니다."),

    // others
    REQUEST_OK(HttpStatus.OK, "올바른 요청입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
    FOR_TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트용 에러입니다.");

    private final HttpStatus status;
    private final String message;
}
