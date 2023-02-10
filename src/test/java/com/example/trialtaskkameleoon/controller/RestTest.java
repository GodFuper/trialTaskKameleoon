package com.example.trialtaskkameleoon.controller;

import com.example.trialtaskkameleoon.dao.*;
import com.example.trialtaskkameleoon.dto.request.CreateUserRequest;
import com.example.trialtaskkameleoon.dto.request.QuoteRequest;
import com.example.trialtaskkameleoon.dto.response.DetailQuoteDto;
import com.example.trialtaskkameleoon.dto.response.ListQuoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.dto.response.SessionDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.Session;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import com.example.trialtaskkameleoon.model.enums.VoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RestTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DebugDao debugDao;

    @Value("${cookie.name}")
    private String cookieName;

    @Test
    void testAllRest() throws ProjectException {

        String cookie = createUser("login", "password");

        Long id = createQuote(cookie, "text");
        modifyQuote(cookie, id, "text2");
        ListQuoteDto listQuoteDto = getQuotes(cookie);
        assertEquals(1, listQuoteDto.getQuotes().size());
        QuoteDto quoteDto = voteUp(cookie, id);
        assertEquals(1, quoteDto.getScore());
        quoteDto = voteDown(cookie, id);
        assertEquals(0, quoteDto.getScore());

        try {
            ResponseEntity response = restTemplate.exchange("/api/quote/random", HttpMethod.GET, new HttpEntity<>(getHeaders(null)), QuoteDto.class);
            assertEquals(200, response.getStatusCodeValue());
            response = restTemplate.exchange("/api/quote/top", HttpMethod.GET, new HttpEntity<>(getHeaders(null)), ListQuoteDto.class);
            assertEquals(200, response.getStatusCodeValue());
            response = restTemplate.exchange("/api/quote/{id}", HttpMethod.GET, new HttpEntity<>(getHeaders(null)), DetailQuoteDto.class, id);
            assertEquals(200, response.getStatusCodeValue());
            response = restTemplate.exchange("/api/quote/{id}", HttpMethod.DELETE, new HttpEntity<>(getHeaders(cookie)), DetailQuoteDto.class, id);
            assertEquals(200, response.getStatusCodeValue());
        } catch (Exception e) {
            fail();
        }

    }

    private HttpHeaders getHeaders(String cookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        if (cookie != null) {
            headers.add(HttpHeaders.COOKIE, cookieName + "=" + cookie);
        }
        return headers;
    }

    private QuoteDto voteUp(String cookie, long id) {
        HttpEntity<Object> request = new HttpEntity<>(getHeaders(cookie));
        ResponseEntity<QuoteDto> response = restTemplate.exchange("/api/quote/{id}/vote/up", HttpMethod.POST, request, QuoteDto.class, id);
        assertEquals(200, response.getStatusCodeValue());
        QuoteDto dto = response.getBody();
        assertNotNull(dto);
        return dto;
    }

    private QuoteDto voteDown(String cookie, long id) {
        HttpEntity<Object> request = new HttpEntity<>(getHeaders(cookie));
        ResponseEntity<QuoteDto> response = restTemplate.exchange("/api/quote/{id}/vote/down", HttpMethod.POST, request, QuoteDto.class, id);
        assertEquals(200, response.getStatusCodeValue());
        QuoteDto dto = response.getBody();
        assertNotNull(dto);
        return dto;
    }

    private String createUser(String login, String password) {
        CreateUserRequest requestDto = new CreateUserRequest(login, password);
        HttpEntity<CreateUserRequest> request = new HttpEntity<>(requestDto);
        ResponseEntity<SessionDto> response = restTemplate.exchange("/api/user", HttpMethod.POST, request, SessionDto.class);
        assertEquals(200, response.getStatusCodeValue());
        SessionDto sessionDto = response.getBody();
        assertNotNull(sessionDto);
        assertNotNull(sessionDto.getCookie());
        HttpHeaders headers = response.getHeaders();
        String cookieHeader = headers.getFirst(HttpHeaders.SET_COOKIE);
        assertNotNull(cookieHeader);
        return sessionDto.getCookie();
    }

    private Long createQuote(String cookie, String text) {
        QuoteRequest requestDto = new QuoteRequest(text);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(requestDto, getHeaders(cookie));
        ResponseEntity<QuoteDto> response = restTemplate.exchange("/api/quote", HttpMethod.POST, request, QuoteDto.class);
        assertEquals(200, response.getStatusCodeValue());
        QuoteDto dto = response.getBody();
        assertNotNull(dto);
        assertNotNull(dto.getId());
        return dto.getId();
    }

    private DetailQuoteDto modifyQuote(String cookie, long id, String text) {
        QuoteRequest requestDto = new QuoteRequest(text);
        HttpEntity<QuoteRequest> request = new HttpEntity<>(requestDto, getHeaders(cookie));
        ResponseEntity<DetailQuoteDto> response = restTemplate.exchange("/api/quote/{id}", HttpMethod.POST, request, DetailQuoteDto.class, id);
        assertEquals(200, response.getStatusCodeValue());
        DetailQuoteDto dto = response.getBody();
        assertNotNull(dto);
        return dto;
    }

    private ListQuoteDto getQuotes(String cookie) {
        HttpEntity request = new HttpEntity<>(getHeaders(cookie));
        ResponseEntity<ListQuoteDto> response = restTemplate.exchange("/api/quote", HttpMethod.GET, request, ListQuoteDto.class);
        assertEquals(200, response.getStatusCodeValue());
        ListQuoteDto dto = response.getBody();
        assertNotNull(dto);
        return dto;
    }

    @BeforeEach
    public void clearAll() {
        debugDao.clearAll();
    }
}