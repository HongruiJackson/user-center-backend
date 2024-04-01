package com.jackson.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author JacksonZHR
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 6044695061421738191L;

    /**
     * 登录账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户校验密码
     */
    private String checkPassword;


}
