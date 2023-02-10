package com.example.trialtaskkameleoon.dao.repository;

import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.QuoteScore;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query("select q as quote, COUNT(distinct vUps) - COUNT(distinct vDowns) as score  from Quote q " +
            "left join Vote vUps on q = vUps.quote and vUps.type = 'UP' left join Vote vDowns on q = vDowns.quote and vDowns.type = 'DOWN' " +
            "where q.user = ?1 " +
            "GROUP BY q")
    Page<QuoteScore> findQuotesByUser(User user, Pageable pageable);

    @Query("select q as quote, COUNT(distinct vUps) - COUNT(distinct vDowns) as score from Quote q " +
            "left join Vote vUps on q = vUps.quote and vUps.type = 'UP' left join Vote vDowns on q = vDowns.quote and vDowns.type = 'DOWN' " +
            "where q = ?1 ")
    QuoteScore findScore(Quote quote);

    @Query("select COUNT(distinct vUps) - COUNT(distinct vDowns) as score from Quote q " +
            "left join Vote vUps on q = vUps.quote and vUps.type = 'UP' AND vUps.create between ?2 and ?3  " +
            "left join Vote vDowns on q = vDowns.quote and vDowns.type = 'DOWN' AND vDowns.create between ?2 and ?3  " +
            "where q = ?1 ")
    Long findScoreBetween(Quote quote, LocalDateTime createStart, LocalDateTime createEnd);


    @Query("select q as quote, COUNT(distinct vUps) - COUNT(distinct vDowns) as score from Quote q " +
            "left join Vote vUps on q = vUps.quote and vUps.type = 'UP' left join Vote vDowns on q = vDowns.quote and vDowns.type = 'DOWN' " +
            "GROUP BY q")
    Page<QuoteScore> findQuotes(Pageable pageable);

    Quote findByUserAndId(User user, Long id);


    @Query("select q as quote, COUNT(distinct vUps) - COUNT(distinct vDowns) as score from Quote q " +
            "left join Vote vUps on q = vUps.quote and vUps.type = 'UP' left join Vote vDowns on q = vDowns.quote and vDowns.type = 'DOWN' " +
            "GROUP BY q order by random()")
    Page<QuoteScore> findRandom(Pageable pageable);

}