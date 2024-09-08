package com.jackson.usercenter.exception;

import com.jackson.usercenter.enums.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{
    private final int code;

    private final String description;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.message);
        this.code = errorCode.code;
        this.description = errorCode.description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.message);
        this.code = errorCode.code;
        this.description = description;
    }

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
