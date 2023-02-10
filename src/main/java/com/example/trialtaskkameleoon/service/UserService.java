package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.SessionDao;
import com.example.trialtaskkameleoon.dao.UserDao;
import com.example.trialtaskkameleoon.dto.request.CreateUserRequest;
import com.example.trialtaskkameleoon.dto.response.SessionDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.mappers.SessionMapper;
import com.example.trialtaskkameleoon.mappers.UserMapper;
import com.example.trialtaskkameleoon.model.Session;
import com.example.trialtaskkameleoon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@Validated
@Transactional(rollbackOn = {ProjectException.class, RuntimeException.class})
public class UserService {
    private final UserDao userDao;
    private final SessionDao sessionDao;
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    @Autowired
    public UserService(UserDao userDao, SessionDao sessionDao, UserMapper userMapper, SessionMapper sessionMapper) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
        this.userMapper = userMapper;
        this.sessionMapper = sessionMapper;
    }

    public SessionDto createUser(@Valid CreateUserRequest request) throws ProjectException {
        User user = userMapper.toUser(request);
        userDao.save(user);
        Session session = new Session(user);
        sessionDao.save(session);
        return sessionMapper.toSessionDto(session);
    }

}
