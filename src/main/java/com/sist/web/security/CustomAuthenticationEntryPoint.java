package com.sist.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Not Authenticated Request");
        log.error("Request URI: {}", request.getRequestURI());
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(requestedWith) ||
                uri.startsWith("/api") ||
                (accept != null && accept.contains("application/json"));
        if (isAjax) {
            //api 요청이거나 json 응답 요청이면
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String json=om.writeValueAsString(ApiResponse.fail(CommonErrorCode.SC_UNAUTHORIZED.getCode(),CommonErrorCode.SC_UNAUTHORIZED.getMessage()));
            response.getWriter().write(json);
        } else{
            response.sendRedirect("/login");
        }

    }
}
