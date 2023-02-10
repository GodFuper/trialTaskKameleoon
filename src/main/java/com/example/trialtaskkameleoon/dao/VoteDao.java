package com.example.trialtaskkameleoon.dao;

import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VoteDao {
    Vote save(Vote vote) throws ProjectException;

    Vote find(Quote quote, User user) throws ProjectException;



    Optional<LocalDateTime> findFirstVoteTime(Quote quote) throws ProjectException;

    Optional<LocalDateTime> findLastVoteTime(Quote quote) throws ProjectException;

    void delete(Vote vote) throws ProjectException;
}
