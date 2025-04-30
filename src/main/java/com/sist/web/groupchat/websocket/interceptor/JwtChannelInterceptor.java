package com.sist.web.groupchat.websocket.interceptor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.sist.web.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwt;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
				log.warn("WebSocket 인증 실패: Authorization 헤더 누락 또는 형식 오류");
				throw new IllegalArgumentException("Authorization 헤더가 없거나 Bearer 형식이 아님");
			}
            
            token = token.substring(7);
            System.out.println(token);
            
            if (!jwt.validateToken(token)) {
				log.warn("WebSocket 인증 실패: 유효하지 않은 토큰");
				throw new IllegalArgumentException("유효하지 않은 토큰");
			}
            
            String userNo = jwt.getUserNoFromToken(token);
            List<String> roles = jwt.getRoles(token);
            List<SimpleGrantedAuthority> authorities = roles.stream()
            		.map(SimpleGrantedAuthority::new)
            		.collect(Collectors.toList());
            
            User principal = new User(userNo, "", authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            accessor.setUser(authentication);
            
            log.info("WebSocket 인증 성공: 사용자 ID = {}", userNo);
        }

        return message;
    }
}
