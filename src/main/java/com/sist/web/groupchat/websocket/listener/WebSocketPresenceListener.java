package com.sist.web.groupchat.websocket.listener;

import com.sist.web.groupchat.service.GroupOnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Slf4j
@Component
public class WebSocketPresenceListener {

    private final GroupOnlineUserService groupOnlineUserService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        groupOnlineUserService.markOffline(sessionId);
        log.info("disconnect: sessionId={}", sessionId);
    }
}