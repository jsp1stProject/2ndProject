package com.sist.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.web.user.mapper.UserMapper;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@Slf4j
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper om;
    private final UserMapper userMapper;

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
            logger.debug(loginRequest.getUsername());
            logger.debug(loginRequest.getPassword());
            //user_mail  -> 현재 유효한 회원의 user_no로 변경
            String userNo = userMapper.getEnableUserNo(loginRequest.getUsername().trim());

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(userNo, loginRequest.getPassword());

            setDetails(request, authRequest);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new RuntimeException("로그인 에러 or 비밀번호 불일치",e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)  {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        //로그인 완료, jwt 발급 시작
        String userNo = authResult.getName();
        Collection<? extends GrantedAuthority> auths= authResult.getAuthorities();
        logger.info(auths.toString());
        String accessToken = jwtTokenProvider.createToken(userNo, auths);
        String refreshToken = jwtTokenProvider.createRefreshToken(userNo);

        response.addCookie(addCk("accessToken", accessToken, 1*60*60*24*7)); //쿠키 유효기간 7일
        response.addCookie(addCk("refreshToken", refreshToken, 1*60*60*24*7));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = "{\"error\":\"password incorrect\"}";
        response.getWriter().write(jsonResponse);
    }

    Cookie addCk(String name, String token, int expire) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(expire);//1시간
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
