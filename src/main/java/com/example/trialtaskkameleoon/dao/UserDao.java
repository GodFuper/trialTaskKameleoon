package com.example.trialtaskkameleoon.dao;

import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.User;

public interface UserDao {
    User save(User user) throws ProjectException;

    void delete(User user) throws ProjectException;
}
