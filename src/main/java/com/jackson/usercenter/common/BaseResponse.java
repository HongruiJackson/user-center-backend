package com.jackson.usercenter.common;

import com.jackson.usercenter.enums.ErrorCode;
import com.jackson.usercenter.enums.SuccessCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private int code; // 业务状态码

    private T data; // 返回的数据

    private String message; // 请求信息

    private String description; // 如果有错误，对于错误的详细描述

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(T data, SuccessCode successCode) {
        this(successCode.code,data,successCode.message,"");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.code,null,errorCode.message,errorCode.message);
    }

}
