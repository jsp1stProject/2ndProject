package com.sist.web.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.security.StompPrincipal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Authorization 헤더가 유효하지 않습니다.");
            }

            String token = authHeader.substring(7);

            try {
                String userNo = jwtTokenProvider.getUserNoFromToken(token);
                List<String> roles = jwtTokenProvider.getRoles(token);
                String role = roles != null && !roles.isEmpty() ? roles.get(0) : "USER";

                accessor.setUser(new StompPrincipal(userNo, role));
                log.debug("STOMP CONNECT - userNo: {}, role: {}", userNo, role);

            } catch (Exception e) {
                log.warn("JWT 인증 실패: {}", e.getMessage());
                throw new IllegalArgumentException("JWT 인증 실패", e);
            }
        }

        return message;
    }
}
