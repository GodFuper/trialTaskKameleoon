package com.example.trialtaskkameleoon.service;

import com.example.trialtaskkameleoon.dao.QuoteDao;
import com.example.trialtaskkameleoon.dao.SessionDao;
import com.example.trialtaskkameleoon.dao.VoteDao;
import com.example.trialtaskkameleoon.dto.response.GraphVoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.exception.ErrorCode;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.mappers.QuoteMapper;
import com.example.trialtaskkameleoon.model.*;
import com.example.trialtaskkameleoon.model.enums.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;

@Service
@Validated
@Transactional(rollbackOn = {ProjectException.class, RuntimeException.class})
public class VoteService extends BaseService {
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private SessionDao sessionDao;
    @Autowired
    private VoteDao voteDao;
    @Autowired
    private QuoteMapper quoteMapper;

    public QuoteDto upVote(@NotNull(message = "Cookie not found") String cookie,
                           @NotNull(message = "Quote id not found") Long id) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();
        Quote quote = quoteDao.findQuote(id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);

        Vote vote = voteDao.find(quote, user);
        if (vote != null && vote.getType() == VoteType.DOWN) {
            voteDao.delete(vote);
        } else if (vote == null) {
            vote = new Vote(user, quote, VoteType.UP);
            voteDao.save(vote);
        }

        QuoteScore quoteScore = quoteDao.findQuoteScore(quote);
        return quoteMapper.toQuoteDto(quoteScore);
    }

    public QuoteDto downVote(@NotNull(message = "Cookie not found") String cookie,
                             @NotNull(message = "Quote id not found") Long id) throws ProjectException {
        Session session = sessionDao.findByCookie(cookie);
        sessionIsNotNull(session, ErrorCode.USER_UNAUTHORIZED);
        User user = session.getUser();
        Quote quote = quoteDao.findQuote(id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);

        Vote vote = voteDao.find(quote, user);
        if (vote != null && vote.getType() == VoteType.UP) {
            voteDao.delete(vote);
        } else if (vote == null) {
            vote = new Vote(user, quote, VoteType.DOWN);
            voteDao.save(vote);
        }

        QuoteScore quoteScore = quoteDao.findQuoteScore(quote);
        return quoteMapper.toQuoteDto(quoteScore);
    }

    public GraphVoteDto getGraphVotes(@NotNull(message = "Quote id not found") Long id) throws ProjectException {
        Quote quote = quoteDao.findQuote(id);
        quoteIsNotNull(quote, ErrorCode.QUOTE_NOT_FOUND);

        Map<LocalDate, Long> mapDateScore = new TreeMap<>();
        LocalDateTime first = voteDao.findFirstVoteTime(quote).orElse(LocalDateTime.now());
        LocalDateTime last = voteDao.findLastVoteTime(quote).orElse(LocalDateTime.now());
        LocalDateTime firstDay = LocalDateTime.of(first.getYear(), first.getMonth(), first.getDayOfMonth(), 0, 0);
        LocalDateTime lastDay = LocalDateTime.of(last.getYear(), last.getMonth(), last.getDayOfMonth() + 1, 0, 0);

        long count = ChronoUnit.DAYS.between(firstDay, lastDay);
        for (int i = 0; i < count; i++) {
            LocalDateTime tmpTo = firstDay.plusDays(i + 1);
            Long score = quoteDao.findScore(quote, firstDay, tmpTo);

            LocalDate dayTarget = firstDay.plusDays(i).toLocalDate();
            mapDateScore.put(dayTarget, score);
        }

        return quoteMapper.toGraphVoteDto(mapDateScore);
    }

}
