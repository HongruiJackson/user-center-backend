package com.jackson.usercenter.controller;

import com.jackson.usercenter.model.domain.User;
import com.jackson.usercenter.model.request.UserLoginRequest;
import com.jackson.usercenter.model.request.UserRegisterRequest;
import com.jackson.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.jackson.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.jackson.usercenter.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) return null;
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) return null;
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, httpServletRequest);
    }

    @GetMapping("/search")
    public List<User> searchUser(String userAccount, HttpServletRequest httpServletRequest) {
        //仅管理员可查
        User  user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if ( user == null || user.getUserRole() != ADMIN_ROLE) return new ArrayList<>();

        return userService.searchUsers(userAccount);
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest httpServletRequest) {
        //仅管理员可删除
        User  user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if ( user == null || user.getUserStatus() != ADMIN_ROLE) return false;
        if (id <= 0) {
            return false;
        }
        return userService.deleteUserById(id);
    }


}
