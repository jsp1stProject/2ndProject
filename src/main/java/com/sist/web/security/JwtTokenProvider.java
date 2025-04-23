package com.sist.web.security;

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
    private final String secretKey="secretKey";
    private final long tokenValidMillisecond = 1000L * 60 * 60; //1시간
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);

    public String createToken(String userNo, Collection<? extends GrantedAuthority> authorities) {
        //토큰 페이로드 설정
        //페이로드 subject=유저일련번호 userNo
        Claims claims = Jwts.claims().setSubject(userNo);

        //GrantedAutority 콜렉션 받아서 String 리스트로 변환, 페이로드에 삽입
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Date now=new Date();
        Date validity=new Date(now.getTime()+tokenValidMillisecond); //만료시간 1시간

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.debug("create token:{}",token);
        return token;
    }

    public String createRefreshToken(String userNo) {
        Claims claims = Jwts.claims().setSubject(userNo);

        Date now=new Date();
        Date validity=new Date(now.getTime()+1000L * 60 * 60 * 24 * 7); //만료시간 7일

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        logger.debug("create refresh token:{}",token);
        return token;
    }

    public boolean validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        //파싱
        //JwtAuthenticationFilter에서 ExpiredJwtException 잡아야하므로 여기선 throw
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
    }

    public Authentication getAuthentication(String token) {
        //토큰 파싱, claim 내용 추출
        String userNo = getUserNoFromToken(token);
        List<String> roles = getRoles(token);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        //user 객체 생성
        User principal = new User(userNo, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal,"", authorities);
    }

    public List<String> getRoles(String token) {
        Claims claims=getClaims(token);
        List<?> rawRoles = claims.get("roles", List.class);
        List<String> roles = rawRoles.stream().map(item -> {
            if (item instanceof String) {
                return (String) item;
            } else if (item instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) item;
                return (String) map.get("authority");
            }
            return "";
        }).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        return roles;
    }

    public String getUserNoFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.debug("Token provider is parsing data from Expired JWT");
            claims = e.getClaims();
            // 만료된 토큰에서 데이터 활용 가능
        }
        return claims;
    }
}
