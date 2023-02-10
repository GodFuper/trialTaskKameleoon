package com.example.trialtaskkameleoon.dao.impl;

import com.example.trialtaskkameleoon.dao.QuoteDao;
import com.example.trialtaskkameleoon.dao.repository.QuoteRepository;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.QuoteScore;
import com.example.trialtaskkameleoon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QuoteDaoImpl implements QuoteDao {

    private final QuoteRepository quoteRepository;

    @Autowired
    public QuoteDaoImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public Quote save(Quote quote) throws ProjectException {
        try {
            return quoteRepository.save(quote);
        } catch (DataIntegrityViolationException e) {
            throw new ProjectException(ErrorCode.QUOTE_EXIST);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public QuoteScore findRandom() throws ProjectException {
        try {
            Pageable pageable = PageRequest.of(0,1);
            Page<QuoteScore> quotes = quoteRepository.findRandom(pageable);
            List<QuoteScore> list = quotes.getContent();
            return list.stream().findFirst().orElse(null);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Quote findQuote(User user, long id) throws ProjectException {
        try {
            return quoteRepository.findByUserAndId(user, id);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Quote findQuote(long id) throws ProjectException {
        try {
            return quoteRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Long findScore(Quote quote, LocalDateTime from, LocalDateTime to) throws ProjectException {
        try {
            return quoteRepository.findScoreBetween(quote, from, to);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public QuoteScore findQuoteScore(Quote quote) throws ProjectException {
        try {
            return quoteRepository.findScore(quote);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Page<QuoteScore> findQuotes(User user, Pageable pageable) throws ProjectException {
        try {
            return quoteRepository.findQuotesByUser(user, pageable);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Page<QuoteScore> findQuotes(Pageable pageable) throws ProjectException {
        try {
            return quoteRepository.findQuotes(pageable);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Quote quote) throws ProjectException {
        try {
            quoteRepository.delete(quote);
        } catch (RuntimeException e) {
            throw new ProjectException(ErrorCode.DATABASE_ERROR);
        }
    }
}
