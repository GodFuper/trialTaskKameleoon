package com.example.trialtaskkameleoon.dao.repository;

import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByQuoteAndUser(Quote quote, User user);

    @Query("select min(v.create) from Vote v where v.quote = ?1")
    Optional<LocalDateTime> getFirstVoteTime(Quote quote);

//    @Query(value = "select v.create from vote v where v.quote = ?1 group by v order by v.create asc limit 1", nativeQuery = true)
//    Optional<LocalDateTime> getFirstVoteTime2(Quote quote);

    @Query("select max(v.create) from Vote v where v.quote = ?1")
    Optional<LocalDateTime> getLastVoteTime(Quote quote);
}