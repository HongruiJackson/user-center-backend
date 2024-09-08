package com.jackson.usercenter.common;

import com.jackson.usercenter.enums.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private int code; // 业务状态码

    private T data; // 返回的数据

    private String message; // 请求信息

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(T data, ResponseEnum responseEnum) {
        this.data = data;
        this.code = responseEnum.code;
        this.message = responseEnum.message;
    }

}
