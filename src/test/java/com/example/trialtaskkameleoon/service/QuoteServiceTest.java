package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.*;
import com.example.trialtaskkameleoon.dao.repository.QuoteRepository;
import com.example.trialtaskkameleoon.dto.request.QuoteRequest;
import com.example.trialtaskkameleoon.dto.response.DetailQuoteDto;
import com.example.trialtaskkameleoon.dto.response.ListQuoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.Session;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import com.example.trialtaskkameleoon.model.enums.VoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class QuoteServiceTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private QuoteDao quoteDao;

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private DebugDao debugDao;

    @Autowired
    private QuoteService quoteService;

    @BeforeEach
    public void clearAll() {
        debugDao.clearAll();
    }

    private Session createUserAndSession(String login) throws ProjectException {
        User user = createUser(login);
        Session session = new Session(user);
        sessionDao.save(session);
        return session;
    }

    private User createUser(String login) throws ProjectException {
        User user = new User(login, login + "pas");
        userDao.save(user);
        return user;
    }

    @Test
    void addQuote() throws ProjectException {
        Session session = createUserAndSession("login");
        String cookie = session.getCookie();
        QuoteRequest request = new QuoteRequest("text");
        QuoteDto quoteDto = quoteService.addQuote(cookie, request);

        assertNotNull(quoteDto.getId());
        assertNotNull(quoteDto.getUser());
        assertNotNull(quoteDto.getUser().getId());
        assertEquals("login", quoteDto.getUser().getLogin());

        Quote quote = quoteDao.findQuote(quoteDto.getId());
        assertEquals("text", quote.getText());
    }

    @Test
    void getQuotes() throws ProjectException {

        Session session = createUserAndSession("login");
        String cookie = session.getCookie();
        for (int i = 0; i < 3; i++) {
            QuoteRequest request = new QuoteRequest("text");
            quoteService.addQuote(cookie, request);
        }


        for (int i = 0; i < 5; i++) {
            Session tmp = createUserAndSession("login" + i);
            String cookieTmp = tmp.getCookie();
            QuoteRequest requestTmp = new QuoteRequest("text");
            quoteService.addQuote(cookieTmp, requestTmp);
        }

        ListQuoteDto response = quoteService.getQuotes(cookie, PageRequest.of(0, 10));
        assertEquals(3, response.getQuotes().size());

        for (QuoteDto quote : response.getQuotes()) {
            assertEquals("login", quote.getUser().getLogin());
        }

        session = sessionDao.findByCookie(cookie);
        assertEquals(3, session.getUser().getQuotes().size());
    }

    @Test
    void modifyQuote() throws ProjectException {
        Session session = createUserAndSession("login");
        User user = session.getUser();
        Quote quote = new Quote(user);
        quote.setText("text");
        quoteDao.save(quote);

        String cookie = session.getCookie();
        QuoteRequest modify = new QuoteRequest("textModify");
        DetailQuoteDto detailQuoteDto = quoteService.modifyQuote(cookie, quote.getId(), modify);

        assertNotNull(detailQuoteDto.getId());
        assertEquals("textModify", detailQuoteDto.getText());

        Quote quoteModify = quoteDao.findQuote(quote.getId());
        assertEquals("textModify", quoteModify.getText());
    }

    @Test
    void deleteQuote() throws ProjectException {
        Session session = createUserAndSession("login");
        User user = session.getUser();
        Quote quote = new Quote(user);
        quote.setText("text");
        quoteDao.save(quote);

        String cookie = session.getCookie();
        quoteService.deleteQuote(cookie, quote.getId());

        assertNull(quoteDao.findQuote(quote.getId()));
    }

    @Test
    void testFindRandom() throws ProjectException {

        for (int i = 0; i < 5; i++) {
            Quote quote1 = new Quote();
            quoteDao.save(quote1);
        }

        Set<Long> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            QuoteDto quoteDto = quoteService.getRandomQuote();
            set.add(quoteDto.getId());
        }
        assertTrue(set.size() > 1);
    }


    @Test
    void testGetTopQuotes() throws ProjectException {
        Session session = createUserAndSession("login");
        User user = session.getUser();
        Quote quote1 = new Quote(user);
        quoteDao.save(quote1);

        //5
        for (int i = 0; i < 5; i++) {
            User userTmp = createUser("random1" + i);
            Vote vote = new Vote(userTmp, quote1, VoteType.UP);
            voteDao.save(vote);
        }

        Quote quote2 = new Quote(user);
        quoteDao.save(quote2);

        //-5
        for (int i = 0; i < 10; i++) {
            User userTmp = createUser("random2" + i);
            Vote vote = new Vote(userTmp, quote2, VoteType.UP);
            voteDao.save(vote);
        }
        for (int i = 0; i < 15; i++) {
            User userTmp = createUser("random3" + i);
            Vote vote = new Vote(userTmp, quote2, VoteType.DOWN);
            voteDao.save(vote);
        }

        Quote quote3 = new Quote(user);
        quoteDao.save(quote3);

        //-2
        for (int i = 0; i < 2; i++) {
            User userTmp = createUser("random4" + i);
            Vote vote = new Vote(userTmp, quote3, VoteType.DOWN);
            voteDao.save(vote);
        }

        Pageable pageable = PageRequest.of(0, 10);
        ListQuoteDto response = quoteService.getQuotes(pageable);
        assertEquals(3, response.getQuotes().size());

        Sort.Order orderAsc = new Sort.Order(Sort.Direction.ASC, "score");
        pageable = PageRequest.of(0, 10, Sort.by(orderAsc));
        response = quoteService.getQuotes(pageable);

        assertEquals(3, response.getQuotes().size());
        assertEquals(-5L, response.getQuotes().get(0).getScore());
        assertEquals(-2L, response.getQuotes().get(1).getScore());
        assertEquals(5L, response.getQuotes().get(2).getScore());

        Sort.Order orderDesc = new Sort.Order(Sort.Direction.DESC, "score");
        pageable = PageRequest.of(0, 10, Sort.by(orderDesc));
        response = quoteService.getQuotes(pageable);

        assertEquals(3, response.getQuotes().size());
        assertEquals(5L, response.getQuotes().get(0).getScore());
        assertEquals(-2L, response.getQuotes().get(1).getScore());
        assertEquals(-5L, response.getQuotes().get(2).getScore());
    }

    @Test
    void getDetailQuote() throws ProjectException {
        Session session = createUserAndSession("login");
        User user = session.getUser();
        Quote quote = new Quote(user);
        quote.setText("text");
        quoteDao.save(quote);

        DetailQuoteDto detailQuoteDto = quoteService.getDetailQuote(quote.getId());

        assertNotNull(detailQuoteDto.getId());
        assertEquals("text", detailQuoteDto.getText());
    }
}