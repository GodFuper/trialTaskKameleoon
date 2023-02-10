package com.example.trialtaskkameleoon.dao.impl;

import com.example.trialtaskkameleoon.dao.SessionDao;
import com.example.trialtaskkameleoon.dao.repository.SessionRepository;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDaoImpl implements SessionDao {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionDaoImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Session save(Session session) throws ProjectException {
        try {
            return sessionRepository.save(session);
        } catch (DataIntegrityViolationException e) {
            throw new ProjectException(ErrorCode.SESSION_EXIST);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Session findByCookie(String cookie) throws ProjectException {
        try {
            return sessionRepository.findByCookie(cookie);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Session session) throws ProjectException {
        try {
            sessionRepository.delete(session);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }
}
