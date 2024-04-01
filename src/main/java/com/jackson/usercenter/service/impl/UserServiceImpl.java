package com.jackson.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.usercenter.model.User;
import com.jackson.usercenter.service.UserService;
import com.jackson.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 10240
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-04-01 10:08:57
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




