package com.example.trialtaskkameleoon.controller;

import com.example.trialtaskkameleoon.dto.request.QuoteRequest;
import com.example.trialtaskkameleoon.dto.response.DetailQuoteDto;
import com.example.trialtaskkameleoon.dto.response.ListQuoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/quote")
public class QuoteController {

    private final QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteDto addQuote(@CookieValue(value = "JAVASESSIONID") String cookie,
                             @RequestBody @Valid QuoteRequest request) throws ProjectException {
        return quoteService.addQuote(cookie, request);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ListQuoteDto getQuotes(@CookieValue(value = "JAVASESSIONID") String cookie,
                                  @RequestParam(value = "order", required = false, defaultValue = "ASC") Sort.Direction direction,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                  @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) throws ProjectException {
        Sort.Order order = new Sort.Order(direction, "create");
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(order));
        return quoteService.getQuotes(cookie, pageable);
    }



    @PostMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DetailQuoteDto modifyQuote(@PathVariable("id") long id,
                                      @CookieValue(value = "JAVASESSIONID") String cookie,
                                      @RequestBody QuoteRequest request) throws ProjectException {
        return quoteService.modifyQuote(cookie, id, request);
    }

    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuote(@PathVariable("id") long id,
                            @CookieValue(value = "JAVASESSIONID") String cookie) throws ProjectException {
        quoteService.deleteQuote(cookie, id);
    }


    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public DetailQuoteDto getDetailQuote(@PathVariable("id") long id) throws ProjectException {
        return quoteService.getDetailQuote(id);
    }

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ListQuoteDto getTopScoreQuotes(@RequestParam(value = "order", required = false, defaultValue = "ASC") Sort.Direction direction,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                          @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) throws ProjectException {
        Sort.Order order = new Sort.Order(direction, "score");
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(order));
        return quoteService.getQuotes(pageable);
    }

    @GetMapping(value = "/random", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteDto getRandomQuote() throws ProjectException {
        return quoteService.getRandomQuote();
    }

}
