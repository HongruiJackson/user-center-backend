package com.jackson.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author JacksonZHR
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 7397836664195396379L;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
