package com.example.trialtaskkameleoon.controller;

import com.example.trialtaskkameleoon.dto.response.GraphVoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quote")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/{id}/vote/up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteDto upVote(@PathVariable("id") long id,
                           @CookieValue(value = "JAVASESSIONID") String cookie) throws ProjectException {
        return voteService.upVote(cookie, id);
    }

    @PostMapping(value = "/{id}/vote/down", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteDto downVote(@PathVariable("id") long id,
                         @CookieValue(value = "JAVASESSIONID") String cookie) throws ProjectException {
        return voteService.downVote(cookie, id);
    }

    @GetMapping(value = "/{id}/vote/graph", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GraphVoteDto getGraphVotes(@PathVariable("id") long id) throws ProjectException {
        return voteService.getGraphVotes(id);
    }
}
