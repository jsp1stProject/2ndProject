package com.sist.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.vo.UserVO;
import io.jsonwebtoken.lang.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper om;
    @Getter
    @Setter
    public static class LoginRequest {
        private String user_mail;
        private String password;
        // getters, setters
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginRequest loginRequest = om.readValue(request.getInputStream(), LoginRequest.class);
//            LoginRequest loginRequest=new LoginRequest();
//            loginRequest.setUser_mail(request.getParameter("user_mail"));
//            loginRequest.setPassword(request.getParameter("password"));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUser_mail(), loginRequest.getPassword());
            return getAuthenticationManager().authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException("로그인 요청 파싱 실패",e);
        }
    }

    @Override
    protected  void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException  {
        String username = authResult.getName();
        Collection<? extends GrantedAuthority> auths= authResult.getAuthorities();
        logger.info(auths.toString());
        String token = jwtTokenProvider.createToken(username, auths);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(om.writeValueAsString(tokenMap));
//        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        // 실패 메시지 예시: "no"
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // JSON 구조를 직접 작성하거나, ObjectMapper를 활용할 수 있습니다.
        String jsonResponse = "{\"error\":\"password incorrect\"}";
        response.getWriter().write(jsonResponse);
    }
}
