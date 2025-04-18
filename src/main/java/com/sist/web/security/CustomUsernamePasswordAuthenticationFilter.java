package com.sist.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper om;

    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            if (!request.getMethod().equals("POST")) {
                throw new AuthenticationServiceException(
                        "Authentication method not supported: " + request.getMethod());
            }

            LoginRequest loginRequest =
                    om.readValue(request.getInputStream(), LoginRequest.class);
            if (loginRequest.getUsername() == null) {
                loginRequest.setUsername("");
            }

            if (loginRequest.getPassword() == null) {
                loginRequest.setPassword("");
            }

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername().trim(), loginRequest.getPassword());

            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new RuntimeException("로그인 에러 or 비밀번호 불일치",e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException  {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        //로그인 완료, jwt 발급 시작
        String username = authResult.getName();
        Collection<? extends GrantedAuthority> auths= authResult.getAuthorities();
        logger.info(auths.toString());
        String token = jwtTokenProvider.createToken(username, auths);

        Map<String, String> tokenMap = new HashMap<>();
        //refresh token 추가 예정
        tokenMap.put("token", token);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(om.writeValueAsString(tokenMap));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = "{\"error\":\"password incorrect\"}";
        response.getWriter().write(jsonResponse);
    }
}
