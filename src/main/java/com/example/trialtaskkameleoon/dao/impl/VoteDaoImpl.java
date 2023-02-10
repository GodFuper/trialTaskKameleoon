package com.example.trialtaskkameleoon.dao.impl;

import com.example.trialtaskkameleoon.dao.VoteDao;
import com.example.trialtaskkameleoon.dao.repository.VoteRepository;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class VoteDaoImpl implements VoteDao {
    private final VoteRepository voteRepository;

    @Autowired
    public VoteDaoImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Vote save(Vote vote) throws ProjectException {
        try {
            return voteRepository.save(vote);
        } catch (DataIntegrityViolationException e) {
            throw new ProjectException(ErrorCode.VOTE_EXIST);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Vote find(Quote quote, User user) throws ProjectException {
        try {
            return voteRepository.findByQuoteAndUser(quote, user);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Optional<LocalDateTime> findFirstVoteTime(Quote quote) throws ProjectException {
        try {
            return voteRepository.getFirstVoteTime(quote);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Optional<LocalDateTime> findLastVoteTime(Quote quote) throws ProjectException {
        try {
            return voteRepository.getLastVoteTime(quote);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Vote vote) throws ProjectException {
        try {
            voteRepository.delete(vote);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }
}
