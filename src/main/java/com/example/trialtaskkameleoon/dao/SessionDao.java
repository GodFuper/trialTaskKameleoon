package com.example.trialtaskkameleoon.dao;

import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Session;

public interface SessionDao {
    Session save(Session session) throws ProjectException;

    Session findByCookie(String cookie) throws ProjectException;

    void delete(Session session) throws ProjectException;
}
