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
    MEMBER_NOT_OWNER(HttpStatus.FORBIDDEN, "소유자가 아닙니다"),

    // auth
    MEMBER_JOIN_REQUIRED(HttpStatus.MULTIPLE_CHOICES, "회원가입이 필요합니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 로그인 토큰입니다."),
    NOT_BEARER_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "Bearer 타입의 토큰이 아닙니다."),
    UNSUPPORTED_TOKEN_TYPE(HttpStatus.UNAUTHORIZED,"지원하지 않는 JWT 형식의 토큰입니다."),
    NEED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    INCORRECT_PASSWORD_OR_ACCOUNT(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸거나, 해당 계정이 없습니다."),
    ACCOUNT_USERNAME_EXIST(HttpStatus.UNAUTHORIZED, "해당 계정이 존재합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "존재하지 않은 리프래쉬 토큰으로 재발급 요청을 했습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프래쉬 토큰입니다."),
    OAUTH2_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth2 제공자 서버에 문제가 발생했습니다."),
    OAUTH2_INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 OAuth2 에러가 발생했습니다."),
    OPEN_ID_PROVIDER_NOT_RESPONSE(HttpStatus.INTERNAL_SERVER_ERROR, "OpenID 제공자 서버에 문제가 발생했습니다."),
    OAUTH2_INVALID_CODE(HttpStatus.BAD_REQUEST, "올바르지 않은 인가 코드입니다."),
    INVALID_SOCIAL_CODE(HttpStatus.BAD_REQUEST, "잘못된 소셜코드입니다."),
    GET_KAKAO_ACCESS_TOKEN_FAILED(HttpStatus.BAD_GATEWAY, "카카오 엑세스 토큰 발급에 실패했습니다."),
    GET_KAKAO_UNIQUE_ID_FAILED(HttpStatus.BAD_GATEWAY, "카카오 유저 정보 흭득에 실패했습니다."),
    GET_NAVER_ACCESS_TOKEN_FAILED(HttpStatus.BAD_GATEWAY, "네이버 엑세스 토큰 발급에 실패했습니다."),
    GET_NAVER_UNIQUE_ID_FAILED(HttpStatus.BAD_GATEWAY, "네이버 유저 정보 흭득에 실패했습니다."),
    GET_GOOGLE_ACCESS_TOKEN_FAILED(HttpStatus.BAD_GATEWAY, "구글 엑세스 토큰 발급에 실패했습니다."),
    GET_GOOGLE_UNIQUE_ID_FAILED(HttpStatus.BAD_GATEWAY, "구글 유저 정보 흭득에 실패했습니다."),

    // file
    EMPTY_LINK(HttpStatus.BAD_REQUEST, "링크가 비어있습니다."),
    EMPTY_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일이 비어있습니다."),
    UNSUPPORTED_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 파일 확장자입니다."),
    IMAGE_STORE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다."),
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지 파일 크기가 너무 큽니다."),
    S3_UPLOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에 파일을 업로드하는 중 에러가 발생했습니다."),
    S3_DOWNLOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에서 파일을 다운로드하는 중 에러가 발생했습니다."),
    S3_DELETE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "S3 버킷에서 파일을 삭제하는 중 에러가 발생했습니다."),
    MALFORMED_URL_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 URL 형식입니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),


    // others
    REQUEST_OK(HttpStatus.OK, "올바른 요청입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 요청입니다."),
    NOT_ENOUGH_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
    FOR_TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트용 에러입니다."),

    // category
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "이 카테고리는 삭제할 수 없습니다."),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 동일한 이름의 카테고리가 존재합니다."),
    CATEGORY_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 카테고리에 대한 접근 권한이 없습니다."),
    CATEGORY_BOOKMARK_NOT_ALLOWED(HttpStatus.FORBIDDEN, "이 카테고리는 북마크할 수 없습니다."),
    CATEGORY_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    CATEGORY_ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "이미 북마크된 카테고리입니다."),
    CATEGORY_INVALID_VISIBILITY(HttpStatus.BAD_REQUEST, "유효하지 않은 공개 설정 값입니다."),
    CATEGORY_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 업데이트 중 오류가 발생했습니다."),
    CATEGORY_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 삭제 중 오류가 발생했습니다."),

    //validation
    NICKNAME_REQUIRED(HttpStatus.BAD_REQUEST, "닉네임은 필수입니다."),
    NICKNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "닉네임은 한글, 영문, 숫자, 공백 포함 10자 이내로 작성해야 합니다."),
    BIRTHDAY_REQUIRED(HttpStatus.BAD_REQUEST, "생년월일은 필수입니다."),
    BIRTHDAY_INVALID(HttpStatus.BAD_REQUEST, "생년월일은 과거 또는 오늘 날짜여야 합니다."),
    GENDER_REQUIRED(HttpStatus.BAD_REQUEST, "성별은 필수입니다."),
    TERMS_OF_SERVICE_REQUIRED(HttpStatus.BAD_REQUEST, "서비스 이용 약관에 동의해야 합니다."),
    PERSONAL_INFORMATION_CONSENT_REQUIRED(HttpStatus.BAD_REQUEST, "개인정보 수집 및 이용에 동의해야 합니다."),

    // content
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "콘텐츠를 찾을 수 없습니다."),
    CONTENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 콘텐츠에 대한 접근 권한이 없습니다."),
    CONTENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 콘텐츠입니다."),
    CONTENT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "콘텐츠 유효성 검사가 실패했습니다."),
    CONTENT_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "콘텐츠 업로드에 실패했습니다."),
    CONTENT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "콘텐츠 수정에 실패했습니다."),
    CONTENT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "콘텐츠 삭제에 실패했습니다."),
    CONTENT_EXPIRED(HttpStatus.GONE, "이 콘텐츠는 만료되었습니다."),
    CONTENT_LOCKED(HttpStatus.LOCKED, "이 콘텐츠는 잠겨 있습니다."),
    CONTENT_QUOTA_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "콘텐츠 저장 한도를 초과했습니다."),
    CONTENT_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 콘텐츠 형식입니다."),
    CONTENT_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "콘텐츠를 보려면 추가 권한이 필요합니다."),
    CONTENT_NOT_PUBLISHED(HttpStatus.FORBIDDEN, "이 콘텐츠는 아직 게시되지 않았습니다."),
    CONTENT_ARCHIVED(HttpStatus.GONE, "이 콘텐츠는 보관 처리되었습니다."),

    // seed
    SEED_NOT_FOUND(HttpStatus.NOT_FOUND, "씨드를 찾을 수 없습니다."),
    SEED_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 씨드 형식입니다."),
    LINK_IS_NECESSARY(HttpStatus.BAD_REQUEST, "링크는 필수입니다."),


    // tag
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그를 찾을 수 없습니다."),
    EMPTY_TAG(HttpStatus.OK, "태그가 존재하지 않습니다."),
    EMPTY_USED_TAG(HttpStatus.OK, "디폴트 태그를 사용한 적 없습니다."),
    TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 태그입니다."),
    TAG_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "태그 생성에 실패했습니다."),
    TAG_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "태그 유효성 검사가 실패했습니다."),
    TAG_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 태그에 대한 접근 권한이 없습니다."),
    TAG_QUOTA_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "태그 저장 한도를 초과했습니다."),
    TAG_USAGE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "이 태그는 사용할 수 없습니다."),
    TAG_ASSOCIATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "태그를 콘텐츠에 연결하는 데 실패했습니다."),
    TAG_DISASSOCIATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "태그를 콘텐츠에서 제거하는 데 실패했습니다."),
    TAG_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "이 태그를 보려면 추가 권한이 필요합니다."),

    // filter
    FILTER_NOT_FOUND(HttpStatus.NOT_FOUND, "필터를 찾을 수 없습니다."),
    EMPTY_FILTER(HttpStatus.OK, "필터가 존재하지 않습니다."),
    FILTER_QUOTA_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "필터 저장 한도를 초과했습니다."),
    FILTER_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "필터 생성에 실패했습니다."),
    FILTER_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "필터 유효성 검사가 실패했습니다."),
    FILTER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "이 필터에 대한 접근 권한이 없습니다."),

    // recommendation
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력 값입니다."),

    // AI
    DAILY_AI_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "AI 분석 요청 횟수를 초과했습니다.");

    private final HttpStatus status;
    private final String message;

    // 메시지를 기반으로 ErrorCode를 찾는 정적 메서드
    public static ErrorCode fromMessage(String message) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getMessage().equals(message)) {
                return errorCode;
            }
        }
        throw SeedzipException.from(ErrorCode.INTERNAL_SEVER_ERROR);
    }
}
