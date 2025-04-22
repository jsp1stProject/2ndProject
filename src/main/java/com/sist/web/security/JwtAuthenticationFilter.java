package com.sist.web.security;

import com.sist.web.common.config.SecurityConfig;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getServletPath();

        //쿠키에서 jwt 추출
        String accessToken=getJwtFromRequest(request,"accessToken");
        String refreshToken=getJwtFromRequest(request,"refreshToken");
        //만료되지 않은 액세스 토큰이 있으면
        if(StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            //권한 추출
            Authentication authentication=jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Valid Token : {}", authentication);

            //유저일련번호, 유저메일, 유저닉네임, 권한을 string으로 attribute에 세팅
            String userNo=jwtTokenProvider.getUserNoFromToken(accessToken);
            UserVO vo=userMapper.getUserMailFromUserNo(userNo);
            System.out.println(vo);
            System.out.println( jwtTokenProvider.getRoles(accessToken).toString());
            request.setAttribute("userno", vo.getUser_no());
            request.setAttribute("usermail", vo.getUser_mail());
            request.setAttribute("nickname", vo.getNickname());
            request.setAttribute("role", jwtTokenProvider.getRoles(accessToken).toString());

        }else if(StringUtils.hasText(accessToken) && !jwtTokenProvider.validateToken(refreshToken)) {
            //액세스 토큰이 있는데 만료됐다면
            if(StringUtils.hasText(refreshToken)) {
                //리프레쉬 토큰이 있는 경우
                log.debug("Invalid Token, Refresh start");
            }else{
                //없는 경우
                log.debug("Invalid Token, You can't access this resource");
            }

        }
        // 공개 경로에 해당하면 필터 로직을 건너뛰고 체인을 바로 진행
        for (String pattern : SecurityConfig.AUTH_WHITELIST) {
            if (pathMatcher.match(pattern, requestPath)) {
                log.debug("공개 URL [{}]", requestPath);
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request,String ckName) {
        //쿠키에서 토큰 추출
        if(request.getCookies() != null || request.getCookies().length > 0) {
            for (Cookie cookie : request.getCookies()) {
                if(ckName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
