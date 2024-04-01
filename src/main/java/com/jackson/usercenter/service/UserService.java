package com.jackson.usercenter.service;

import com.jackson.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author JacksonZHR
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-04-01 10:08:57
*/
public interface UserService extends IService<User> {


    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 用户二次确认的校验密码
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

}
