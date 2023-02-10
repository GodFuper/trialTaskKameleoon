package com.example.trialtaskkameleoon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.Cookie;
import java.util.UUID;

@Service
@Validated
public class CookieService {
    @Value("${cookie.name}")
    private String cookieName;


    public Cookie createCookie() {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(60 * 60);
        return cookie;
    }

    public Cookie deleteCookie() {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        return cookie;
    }
}
