package com.sist.web.chat.websocket.config;

import java.util.Date;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	private final String secretKey = "thisiskey";
	private final long expiration = 1000 * 60 * 60 * 3;
	
	// token 생성
    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }
    // 파싱된 token 의 subject 에 있는 userId 반환
    public String getUserIdFromToken(String token) {
        return getClaims(token).getSubject(); // subject에 userId 저장됨
    }

    public boolean validateToken(String token) {
        try {
        	Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
	// token 파싱하여 payload 정보 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
