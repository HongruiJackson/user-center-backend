package com.jackson.usercenter.utils;

import com.jackson.usercenter.common.BaseResponse;
import com.jackson.usercenter.enums.ErrorCode;
import com.jackson.usercenter.enums.SuccessCode;

public class ResultUtils {
    public static <T>BaseResponse<T> success(T data, SuccessCode successCode) {
        return new BaseResponse<>(data,successCode);
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int errorCode, String message, String description) {
        return new BaseResponse<>(errorCode,null,message,description);
    }
}
