package com.example.trialtaskkameleoon.dao.impl;

import com.example.trialtaskkameleoon.dao.DebugDao;
import com.example.trialtaskkameleoon.dao.repository.QuoteRepository;
import com.example.trialtaskkameleoon.dao.repository.SessionRepository;
import com.example.trialtaskkameleoon.dao.repository.UserRepository;
import com.example.trialtaskkameleoon.dao.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DebugDaoImpl implements DebugDao {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final QuoteRepository quoteRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public DebugDaoImpl(UserRepository userRepository, SessionRepository sessionRepository, QuoteRepository quoteRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.quoteRepository = quoteRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public void clearAll() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
        quoteRepository.deleteAll();
        voteRepository.deleteAll();
    }
}
