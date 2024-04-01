package com.jackson.usercenter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegisterTest() {
        String userAccount = "jackson";
        String userPassword = "jdaisjdioa";
        String checkPassword = "jdaisjdioa";
        long l = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(0,l);

        userAccount = "jackson@";
        long l2 = userService.userRegister(userAccount, userPassword, checkPassword);
        assertEquals(-1, l2);
    }


    @Test
    void userRegister() {
        String userAccount = "jackson";
        String userPassword = "123456789";
        String checkPassword = "123456789";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        assertNotEquals(-1, result);
    }
}