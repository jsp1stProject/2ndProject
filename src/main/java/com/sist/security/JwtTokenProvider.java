package com.sist.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String secretKey="secretKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60; //1시간
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);

    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        Date now=new Date();
        Date validity=new Date(now.getTime()+tokenValidMillisecond);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.info("create token:{}",token);
        return token;
    }

    public boolean validateToken(String token) {
        try{
            //파싱
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){
            logger.info("Expired JWT token: ", e.getMessage());
        } catch (SignatureException | MalformedJwtException e){
            logger.info("Invalid JWT token: ", e.getMessage());
        } catch (Exception e){
            logger.error("JWT validation error: ", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        //토큰 파싱, 내용 추출
        Claims claims=Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        String username=claims.getSubject();
        List<?> rawRoles = claims.get("roles", List.class);
        List<String> roles = rawRoles.stream().map(item -> {
            if (item instanceof String) {
                return (String) item;
            } else if (item instanceof Map) {
                // 여기서 Map은 SimpleGrantedAuthority의 직렬화된 형태일 수 있고,
                // 일반적으로 "authority" 키가 존재합니다.
                Map<?, ?> map = (Map<?, ?>) item;
                return (String) map.get("authority");
            }
            return "";
        }).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        logger.info(authorities.toString());
        //user 객체 생성
        User principal = new User(username, "", authorities);
        logger.info(principal.toString());

        return new UsernamePasswordAuthenticationToken(principal,"", authorities);
    }


}
