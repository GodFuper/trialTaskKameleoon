package com.example.trialtaskkameleoon.controller;

import com.example.trialtaskkameleoon.dto.request.CreateUserRequest;
import com.example.trialtaskkameleoon.dto.response.SessionDto;
import com.example.trialtaskkameleoon.exception.ProjectException;
import com.example.trialtaskkameleoon.service.UserService;
import com.example.trialtaskkameleoon.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final CookieService cookieService;

    @Autowired
    public UserController(UserService userService, CookieService cookieService) {
        this.userService = userService;
        this.cookieService = cookieService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SessionDto createUser(@RequestBody CreateUserRequest request, HttpServletResponse response) throws ProjectException {
        Cookie cookie = cookieService.createCookie();
        SessionDto session = userService.createUser(request);
        cookie.setValue(session.getCookie());
        response.addCookie(cookie);
        return session;
    }
}
