package com.jackson.usercenter.enums;

/**
 * 通用的响应的code、message封装
 */
public enum ResponseEnum {

    COMMON_SUCCESS(0,"ok"),

    ;
    public final int code;

    public final String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
