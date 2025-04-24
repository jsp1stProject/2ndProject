package com.sist.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        log.error("Not Authenticated Request", authException);
        log.error("Request URI: {}", request.getRequestURI());
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        String requestedWith = request.getHeader("X-Requested-With");

        boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(requestedWith) ||
                uri.startsWith("/api") ||
                (accept != null && accept.contains("application/json"));
        if (isAjax) {
            //api 요청이거나 json 응답 요청이면
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, String> map = new HashMap<>();
            map.put("error", "401");
            map.put("message", "로그인이 필요합니다.");
            response.getWriter().write(om.writeValueAsString(map));
            response.getWriter().flush();
            response.getWriter().close();
        } else{
            request.setAttribute("message", "로그인이 필요합니다.");
            request.setAttribute("error", HttpServletResponse.SC_UNAUTHORIZED);
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/main/error.jsp");
            rd.forward(request, response);
        }

    }
}
