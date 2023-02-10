package com.example.trialtaskkameleoon.dao.impl;

import com.example.trialtaskkameleoon.dao.UserDao;
import com.example.trialtaskkameleoon.dao.repository.UserRepository;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    @Autowired
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) throws ProjectException {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ProjectException(ErrorCode.LOGIN_ALREADY_EXISTS);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(User user) throws ProjectException {
        try {
            userRepository.delete(user);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }
}
