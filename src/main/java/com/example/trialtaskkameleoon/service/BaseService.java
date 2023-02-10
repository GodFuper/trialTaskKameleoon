package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.Session;

public class BaseService {
    protected void sessionIsNotNull(Session session, ErrorCode errorCode) throws ProjectException {
        if (session == null) {
            throw new ProjectException(errorCode);
        }
    }

    protected void quoteIsNotNull(Quote quote, ErrorCode errorCode) throws ProjectException {
        if (quote == null) {
            throw new ProjectException(errorCode);
        }
    }
}
