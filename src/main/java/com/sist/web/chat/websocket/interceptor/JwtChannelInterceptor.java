package com.sist.web.chat.websocket.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import com.sist.web.chat.websocket.config.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwt;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("WebSocket CONNECT ��û: ��ū = {}", token);
            if (token != null) {
				if (token.startsWith("Bearer ")) {
					token = token.substring(7);
				}
				if (jwt.validateToken(token)) {
					String userId = jwt.getUserIdFromToken(token);
					accessor.setUser(new StompPrincipal(userId)); // Principal �� userId ����
				} else {
					log.warn("WebSocket ���� ����: ��ȿ���� ���� ��ū");
					throw new IllegalArgumentException("��ū ��ȿ�� �˻� ����");
				}
			} else {
				log.warn("WebSocket ���� ����: ��ū ���� �Ǵ� Bearer ����");
				throw new IllegalArgumentException("Authoriaztion ��� ����");
			}
        }

        return message;
    }
}
