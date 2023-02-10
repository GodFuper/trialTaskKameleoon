package com.example.trialtaskkameleoon.dao;

import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.QuoteScore;
import com.example.trialtaskkameleoon.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface QuoteDao {

    Quote save(Quote quote) throws ProjectException;

    QuoteScore findRandom() throws ProjectException;

    Quote findQuote(User user, long id) throws ProjectException;

    Quote findQuote(long id) throws ProjectException;

    Long findScore(Quote quote, LocalDateTime from, LocalDateTime to) throws ProjectException;

    QuoteScore findQuoteScore(Quote quote) throws ProjectException;

    Page<QuoteScore> findQuotes(User user, Pageable pageable) throws ProjectException;

    Page<QuoteScore> findQuotes(Pageable pageable) throws ProjectException;

    void delete(Quote quote) throws ProjectException;
}
