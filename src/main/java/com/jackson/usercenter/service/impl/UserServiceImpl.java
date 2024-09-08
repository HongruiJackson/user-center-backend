package com.jackson.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.usercenter.enums.ErrorCode;
import com.jackson.usercenter.exception.BusinessException;
import com.jackson.usercenter.model.domain.User;
import com.jackson.usercenter.service.UserService;
import com.jackson.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.jackson.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author JacksonZHR
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-04-01 10:08:57
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    static final String SALT = "dwoiwxnl[qzr34,mx102xb=1";



    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验
        //1.1 非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        //1.2 业务逻辑： 登录的账户不小于4位、
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号小于4位");
        //1.3 业务逻辑： 登录的密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");

        //1.4 密码和校验密码是否相同
        if (!userPassword.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不同");

        //1.5 账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.find()) {
            log.info("包含特殊符号");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号含有特殊符号");
        }

        //1.6 账户不允许重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }

        //2. 密码加密
        String encryptedPassword = DigestUtils.sha256Hex(SALT + userPassword);

        //3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        int insert = userMapper.insert(user);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DB_ERROR,"检查数据库日志");
        }


        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        //1.1 非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        //1.2 业务逻辑： 登录的账户不小于4位、
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号小于4位");
        //1.3 业务逻辑： 登录的密码不小于8位
        if (userPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");

        //1.4 账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.find()) {
            log.info("包含特殊符号");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号含有特殊符号");
        }

        //2. 密码加密
        String encryptedPassword = DigestUtils.sha256Hex(SALT + userPassword);

        //3. 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount).eq("user_password", encryptedPassword);
        User user = userMapper.selectOne(queryWrapper);
        //3.1 若用户不存在
        if (user == null) {
            log.info("login failed, user is not exited or wrong password");
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");
        }

        //4. 用户脱敏
        User anonymizedUser = getSafetyUser(user);

        //5. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, anonymizedUser);

        return anonymizedUser;
    }

    /**
     * 用户信息脱敏
     * @param user 数据库用户信息
     * @return 脱敏后的用户信息
     */
    public User getSafetyUser(User user) {
        if (user == null) throw new BusinessException(ErrorCode.NULL_ERROR,"用户不存在");;
        User anonymizedUser = new User();
        anonymizedUser.setId(user.getId());
        anonymizedUser.setUserAccount(user.getUserAccount());
        anonymizedUser.setUsername(user.getUsername());
        anonymizedUser.setAvatarUrl(user.getAvatarUrl());
        anonymizedUser.setGender(user.getGender());
        anonymizedUser.setPhone(user.getPhone());
        anonymizedUser.setEmail(user.getEmail());
        anonymizedUser.setUserStatus(user.getUserStatus());
        anonymizedUser.setUserRole(user.getUserRole());
        return anonymizedUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public List<User> searchUsers(String userAccount) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_account", userAccount);
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().peek(user -> user.setUserPassword(null)).toList();

    }

    @Override
    public boolean deleteUserById(long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        int delete = userMapper.delete(queryWrapper);
        return delete == 1;
    }
}




