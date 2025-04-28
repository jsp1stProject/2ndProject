package com.sist.web.chat.websocket.listener;

import java.security.Principal;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.sist.web.chat.group.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketPresenceListener {
	
	private final OnlineUserService onlineUserService;
	private final SimpMessagingTemplate messagingTemplate;
	
	@EventListener
	public void handleConnect(SessionConnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = accessor.getUser();
		
		if (user != null) {
			long userNo = Long.parseLong(user.getName());
			onlineUserService.markOnline(userNo);
			log.info("접속: userNo = {}", userNo);
			
			messagingTemplate.convertAndSend("/pub/groups/online", onlineUserService.getOnlineUsers());
		}
	}
	
	@EventListener
	public void handleDisconnect(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = accessor.getUser();
		
		if (user != null) {
			long userNo = Long.parseLong(user.getName());
			onlineUserService.markOffline(userNo);
			log.info("해제: userNo = {}", userNo);
			
			messagingTemplate.convertAndSend("/topic/groups/online", onlineUserService.getOnlineUsers());
		}
	}
}
