package com.sist.web.sitterchat.controller;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthInfoController {
    private final JwtTokenProvider jwt;

    @GetMapping("/me")
    public Map<String, Object> getLoginUser(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String token = resolveTokenFromCookie(request); // 쿠키에서 accessToken 가져옴

        if (token == null) {
            map.put("valid", false);
            return map;
        }

        try {
            String userNo = jwt.getUserNoFromToken(token);
            map.put("userNo", Long.parseLong(userNo));
            map.put("valid", true);
            map.put("token", token);
        } catch (Exception e) {
            map.put("valid", false);
        }

        return map;
    }
    
    private String resolveTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
