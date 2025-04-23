package com.sist.web.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {
        response.setCharacterEncoding("utf-8");
        try{
            filterChain.doFilter(request, response);
        } catch (SignatureException | MalformedJwtException e){ //훼손된 토큰 예외는 여기서 잡기 / 만료된 토큰은 재발급 해야해서 잡지 않음
            log.error("SignatureException | MalformedJwtException JWT Token", request.getRequestURI());
        }
    }
}
