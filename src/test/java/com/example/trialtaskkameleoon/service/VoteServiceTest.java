package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.*;
import com.example.trialtaskkameleoon.dto.response.GraphVoteDto;
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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class VoteServiceTest {
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
    private VoteService voteService;

    @Test
    void upVote() throws ProjectException {
        User user = createUser("test");
        Quote quote = createQuote(user);

        assertEquals(0, quoteDao.findQuoteScore(quote).getScore());

        Session session = createUserAndSession("login");
        String cookie = session.getCookie();


        QuoteDto response = voteService.upVote(cookie, quote.getId());
        assertNotNull(response.getId());
        assertEquals(1, response.getScore());
        assertNotNull(response.getUser());
        assertEquals("test", response.getUser().getLogin());
    }

    @Test
    void upVoteAndVoteDown() throws ProjectException {
        User user = createUser("test");
        Quote quote = createQuote(user);

        assertEquals(0, quoteDao.findQuoteScore(quote).getScore());

        Session session = createUserAndSession("login");
        String cookie = session.getCookie();

        QuoteDto response = voteService.upVote(cookie, quote.getId());
        assertEquals(1, response.getScore());

        response = voteService.downVote(cookie, quote.getId());
        assertEquals(0, response.getScore());
    }

    @Test
    void downVote() throws ProjectException {
        User user = createUser("test");
        Quote quote = createQuote(user);

        assertEquals(0, quoteDao.findQuoteScore(quote).getScore());

        Session session = createUserAndSession("login");
        String cookie = session.getCookie();

        QuoteDto response = voteService.downVote(cookie, quote.getId());
        assertNotNull(response.getId());
        assertEquals(-1, response.getScore());
        assertNotNull(response.getUser());
        assertEquals("test", response.getUser().getLogin());
    }

    @Test
    void downVoteAndVoteUp() throws ProjectException {
        User user = createUser("test");
        Quote quote = createQuote(user);

        assertEquals(0, quoteDao.findQuoteScore(quote).getScore());

        Session session = createUserAndSession("login");
        String cookie = session.getCookie();

        QuoteDto response = voteService.downVote(cookie, quote.getId());
        assertEquals(-1, response.getScore());

        response = voteService.upVote(cookie, quote.getId());
        assertEquals(0, response.getScore());
    }

    private LocalDateTime createFromLocalDate(LocalDate localDate) {
        Random random = new Random();
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(),random.nextInt(22), random.nextInt(59));
    }

    @Test
    void getGraphVotes() throws ProjectException {
        Quote quote = createQuote(null);

        LocalDate firstDay = LocalDate.of(2023, 1, 1);


        for (int i = 0; i < 10; i++) {
            Vote vote = createVote(null, quote, VoteType.UP);
            vote.setCreate(createFromLocalDate(firstDay));
            voteDao.save(vote);
        }

        LocalDate threeDay = firstDay.plusDays(2);
        for (int i = 0; i < 5; i++) {
            Vote vote = createVote(null, quote, VoteType.DOWN);
            vote.setCreate(createFromLocalDate(threeDay));
            voteDao.save(vote);
        }

        LocalDate sixDay = firstDay.plusDays(5);

        for (int i = 0; i < 5; i++) {
            Vote vote = createVote(null, quote, VoteType.DOWN);
            vote.setCreate(createFromLocalDate(sixDay));
            voteDao.save(vote);
        }

        LocalDate eightDay = firstDay.plusDays(7);

        for (int i = 0; i < 20; i++) {
            Vote vote = createVote(null, quote, VoteType.UP);
            vote.setCreate(createFromLocalDate(eightDay));
            voteDao.save(vote);
        }

        LocalDate tenDay = firstDay.plusDays(9);

        for (int i = 0; i < 5; i++) {
            Vote vote = createVote(null, quote, VoteType.UP);
            vote.setCreate(createFromLocalDate(tenDay));
            voteDao.save(vote);
        }


        GraphVoteDto response = voteService.getGraphVotes(quote.getId());
        assertNotNull(response.getMap());
        assertEquals(10, response.getMap().size());

        List<Long> score = Arrays.asList(10L, 10L, 5L, 5L, 5L, 0L, 0L, 20L, 20L, 25L);
        int i = 0;
        for (Long value : response.getMap().values()) {
            assertEquals(score.get(i++), value);
        }

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

    private Quote createQuote(User user) throws ProjectException {
        Quote quote = new Quote(user);
        quoteDao.save(quote);
        return quote;
    }

    private Vote createVote(User user, Quote quote, VoteType type) throws ProjectException {
        Vote vote = new Vote(user, quote, type);
        voteDao.save(vote);
        return vote;
    }

    @BeforeEach
    public void clearAll() {
        debugDao.clearAll();
    }
}