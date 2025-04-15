package com.sist.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey="secretKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60; //1시간
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now=new Date();
        Date validity=new Date(now.getTime()+tokenValidMillisecond);

        String token = Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
        logger.info("토큰생성:{}",token);
        return token;
    }

    public boolean validateToken(String token) {
        try{
            //파싱
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (SignatureException | MalformedJwtException e){
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (Exception e){
            System.out.println("JWT validation error: " + e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        //토큰 파싱, 내용 추출
        Claims claims=Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String username=claims.getSubject();
        SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_USER");
        return new UsernamePasswordAuthenticationToken(username,"", Collections.singletonList(authority));
    }


}
