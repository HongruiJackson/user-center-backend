package com.jackson.usercenter.controller;

import com.jackson.usercenter.common.BaseResponse;
import com.jackson.usercenter.enums.ResponseEnum;
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

    /**
     * 用户注册功能
     * @param userRegisterRequest 用户注册请求体信息
     * @return 注册情况
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) return null;
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return new BaseResponse<>(result, ResponseEnum.COMMON_SUCCESS);
    }

    /**
     * 用户登录
     * @param userLoginRequest 用户登录请求体
     * @param httpServletRequest http请求，set Session的状态
     * @return 用户脱敏信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) return null;
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return new BaseResponse<>(user, ResponseEnum.COMMON_SUCCESS);

    }

    /**
     * 用户注销
     * 移除session中的登录态
     * @param httpServletRequest http请求 remove
     * @return 无效值
     */
    @PostMapping("/logOUt")
    public BaseResponse<Integer> userLogOut(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) return null;
        Integer i = userService.userLogout(httpServletRequest);
        return new BaseResponse<>(i, ResponseEnum.COMMON_SUCCESS);
    }

    /**
     * 获取用户登录态
     * @param httpServletRequest http请求
     * @return 用户信息
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest httpServletRequest) {
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUserInSession = (User) userObj;
        if (currentUserInSession == null) {
            return null;
        }
        long userId = currentUserInSession.getId();
        User userInDB = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(userInDB);
        return new BaseResponse<>(safetyUser,ResponseEnum.COMMON_SUCCESS);
    }


    /**
     * 查询用户
     * 允许管理员能够模糊查询用户
     * @param userAccount 要查询的用户
     * @param httpServletRequest http请求
     * @return 脱敏用户信息列表
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String userAccount, HttpServletRequest httpServletRequest) {
        //仅管理员可查
        User  user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if ( user == null || user.getUserRole() != ADMIN_ROLE)
            return new BaseResponse<>(new ArrayList<>(), ResponseEnum.COMMON_SUCCESS);

        List<User> userList = userService.searchUsers(userAccount);
        return new BaseResponse<>(userList, ResponseEnum.COMMON_SUCCESS);
    }

    /**
     * 删除用户
     * 管理员能够根据id删除用户
     * @param id 用户id
     * @param httpServletRequest http请求体
     * @return 删除是否成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest httpServletRequest) {
        //仅管理员可删除
        User  user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if ( user == null || user.getUserStatus() != ADMIN_ROLE)
            return new BaseResponse<>(false, ResponseEnum.COMMON_SUCCESS);
        if (id <= 0) {
            return new BaseResponse<>(false, ResponseEnum.COMMON_SUCCESS);
        }
        boolean result = userService.deleteUserById(id);

        return new BaseResponse<>(result, ResponseEnum.COMMON_SUCCESS);
    }


}
