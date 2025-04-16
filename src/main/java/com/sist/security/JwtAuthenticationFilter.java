package com.sist.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final List<String> excludedPaths = Arrays.asList("/assets/**","/login/**","/join/**","/main/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();

        // 공개 경로에 해당하면 필터 로직을 건너뛰고 체인을 바로 진행
        for (String pattern : excludedPaths) {
            if (pathMatcher.match(pattern, requestPath)) {
                log.debug("공개 URL [{}] 에 접근, JWT 검증 건너뜀", requestPath);
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token=getJwtFromRequest(request);
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Authentication authentication=jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Valid Token : {}", authentication);
        }else{
            log.debug("No Token Found or Invalid Token");
        }
        filterChain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        System.out.println(request.getCookies().length);
        if(request.getCookies() != null || request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());
                if("login_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
