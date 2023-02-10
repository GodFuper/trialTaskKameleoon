package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.QuoteDao;
import com.example.trialtaskkameleoon.dao.SessionDao;
import com.example.trialtaskkameleoon.dto.request.QuoteRequest;
import com.example.trialtaskkameleoon.dto.response.DetailQuoteDto;
import com.example.trialtaskkameleoon.dto.response.ListQuoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.mappers.QuoteMapper;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.QuoteScore;
import com.example.trialtaskkameleoon.model.Session;
import com.example.trialtaskkameleoon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Service
@Validated
@Transactional(rollbackOn = {ProjectException.class, RuntimeException.class})
public class QuoteService extends BaseService {

    private final QuoteDao quoteDao;

    private final SessionDao sessionDao;

    private final QuoteMapper quoteMapper;

    @Autowired
    public QuoteService(QuoteDao quoteDao, SessionDao sessionDao, QuoteMapper quoteMapper) {
        this.quoteDao = quoteDao;
        this.sessionDao = sessionDao;
        this.quoteMapper = quoteMapper;
    }

    public QuoteDto addQuote(@NotNull(message = "Cookie not found") String cookie, @Valid QuoteRequest request) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();
        Quote quote = new Quote(user);
        quoteMapper.updateQuote(request, quote);
        quoteDao.save(quote);
        return quoteMapper.toQuoteDto(quote);
    }

    public QuoteDto getRandomQuote() throws ProjectException {
        QuoteScore quote = quoteDao.findRandom();
        return quoteMapper.toQuoteDto(quote);
    }

    public ListQuoteDto getQuotes(@NotNull(message = "Cookie not found") String cookie,
                                  @NotNull Pageable pageable) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();

        Page<QuoteScore> page = quoteDao.findQuotes(user, pageable);
        return quoteMapper.toListQuoteDto(page.getContent(), page.getTotalElements());
    }

    public DetailQuoteDto modifyQuote(@NotNull(message = "Cookie not found") String cookie,
                                      @NotNull(message = "Quote id not found") Long id,
                                      @Valid QuoteRequest request) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();
        Quote quote = quoteDao.findQuote(user, id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);
        quoteMapper.updateQuote(request, quote);
        return quoteMapper.toDetailQuoteDto(quote);
    }

    public void deleteQuote(@NotNull(message = "Cookie not found") String cookie,
                            @NotNull(message = "Quote id not found") Long id) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();
        Quote quote = quoteDao.findQuote(user, id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);

        quoteDao.delete(quote);
    }



    public ListQuoteDto getQuotes(@NotNull Pageable pageable) throws ProjectException {
        Page<QuoteScore> page = quoteDao.findQuotes(pageable);
        return quoteMapper.toListQuoteDto(page.getContent(), page.getTotalElements());
    }

    public DetailQuoteDto getDetailQuote(@NotNull(message = "Quote id not found") Long id) throws ProjectException {
        Quote quote = quoteDao.findQuote(id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);
        return quoteMapper.toDetailQuoteDto(quote);
    }



}
