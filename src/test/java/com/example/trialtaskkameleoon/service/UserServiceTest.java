package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.DebugDao;
import com.example.trialtaskkameleoon.dao.UserDao;
import com.example.trialtaskkameleoon.dto.request.CreateUserRequest;
import com.example.trialtaskkameleoon.dto.response.SessionDto;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DebugDao debugDao;

    @Autowired
    private UserService userService;


    @BeforeEach
    public void clearAll() {
        debugDao.clearAll();
    }

    @Test
    void createUser() throws ProjectException {
        CreateUserRequest request = new CreateUserRequest("login", "password");
        SessionDto sessionDto = userService.createUser(request);

        Assertions.assertNotNull(sessionDto.getCookie());
        Assertions.assertNotNull(sessionDto.getUser());
        Assertions.assertEquals("login", sessionDto.getUser().getLogin());
        Assertions.assertNotNull(sessionDto.getUser().getId());
    }

    @Test
    void createUserException() throws ProjectException {
        CreateUserRequest request = new CreateUserRequest("login", "password");
        userDao.save(new User("login", "pass"));
        try {
            userService.createUser(request);
            fail();
        } catch (ProjectException e) {
            Assertions.assertEquals(ErrorCode.LOGIN_ALREADY_EXISTS, e.getErrorCode());
        }
    }
}