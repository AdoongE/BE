package com.adoonge.seedzip.global.exception;

import lombok.Getter;

@Getter
public class SeedzipException extends RuntimeException {
    private final ErrorCode errorCode;
    private String message;

    private SeedzipException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private SeedzipException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public static SeedzipException from(ErrorCode errorCode) {
        return new SeedzipException(errorCode);
    }

    public static SeedzipException from(ErrorCode errorCode,String message) {
        return new SeedzipException(errorCode,message);
    }
}
