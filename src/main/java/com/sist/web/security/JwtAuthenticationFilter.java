package com.sist.web.security;

import com.sist.web.common.config.SecurityConfig;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.Collection;
import java.util.List;

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

        // static resource에 해당하면 필터 로직을 건너뛰고 체인을 바로 진행
        if (pathMatcher.match("/assets/**", requestPath)) {
//            log.debug("resource [{}]", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        //쿠키에서 jwt 추출
        String accessToken=getJwtFromRequest(request,"accessToken");
        String refreshToken=getJwtFromRequest(request,"refreshToken");

        //액세스 쿠키 만료와 관계없이 권한, 유저정보 미리 추출
        Authentication authentication;
        String userNo;
        UserVO vo;
        List<String> roles;
        if(StringUtils.hasText(accessToken)){
            authentication=jwtTokenProvider.getAuthentication(accessToken);
            userNo=jwtTokenProvider.getUserNoFromToken(accessToken);
            vo=userMapper.getUserMailFromUserNo(userNo);
            roles=jwtTokenProvider.getRoles(accessToken);

            try{
                //Access token 검증
                jwtTokenProvider.validateToken(accessToken);

                //유효한 토큰이면 security context에 권한 set
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Valid Token : {}", authentication);
                log.debug("Request URI: {}", request.getRequestURI());

                //유저일련번호, 유저메일, 유저닉네임, 권한을 attribute에 세팅
                request.setAttribute("userno", vo.getUser_no());
                request.setAttribute("usermail", vo.getUser_mail());
                request.setAttribute("nickname", vo.getNickname());
                request.setAttribute("role", roles);

            }catch (ExpiredJwtException e){
                //만료됐으면
                log.debug("Invalid Token, Refresh start");
                //리프레쉬 토큰 검증
                try{
                    jwtTokenProvider.validateToken(refreshToken);

                    Collection<? extends GrantedAuthority> auths= authentication.getAuthorities();
                    logger.info(auths.toString());
                    //액세스,리프레쉬 토큰 재발급
                    accessToken = jwtTokenProvider.createToken(userNo, auths);
                    refreshToken = jwtTokenProvider.createRefreshToken(userNo);

                    response.addCookie(addCk("accessToken", accessToken, 1*60*60*24*7)); //쿠키 유효기간 7일
                    response.addCookie(addCk("refreshToken", refreshToken, 1*60*60*24*7));

                    //security context에 권한 set
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    //유저일련번호, 유저메일, 유저닉네임, 권한을 attribute에 세팅
                    request.setAttribute("userno", vo.getUser_no());
                    request.setAttribute("usermail", vo.getUser_mail());
                    request.setAttribute("nickname", vo.getNickname());
                    request.setAttribute("role", jwtTokenProvider.getRoles(accessToken));

                } catch (Exception e2){
                    log.debug("Invalid refresh Token. Can't refresh");
                    removeAttr(request);
                }

            } catch (SignatureException | MalformedJwtException e){
                log.debug("MalformedJwtException");
                removeAttr(request);
            } catch (Exception e){
                removeAttr(request);
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

    Cookie addCk(String name, String token, int expire) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(expire);//1시간
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    void removeAttr(HttpServletRequest request){
        request.removeAttribute("userno");
        request.removeAttribute("usermail");
        request.removeAttribute("nickname");
        request.removeAttribute("role");
    }
}
