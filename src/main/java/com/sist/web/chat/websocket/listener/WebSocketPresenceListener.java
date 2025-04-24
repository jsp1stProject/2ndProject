package com.sist.web.chat.websocket.listener;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.sist.web.common.util.OnlineUserManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketPresenceListener {
	@EventListener
	public void handleConnect(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		Principal user = accessor.getUser();
		
		if (user != null) {
			String userNo = user.getName();
			OnlineUserManager.addSession(userNo, sessionId);
			log.info("접속: userNo = {}, sessionId = {}", userNo, sessionId);
		}
	}
	
	@EventListener
	public void handleDisconnect(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		OnlineUserManager.removeSession(sessionId);
		log.info("연결 종료: sessionId = {}", sessionId);
	}
}
