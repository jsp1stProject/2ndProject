package com.sist.web.groupchat.controller;

import com.sist.web.groupchat.dto.GroupJoinDTO;
import com.sist.web.groupchat.service.GroupOnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Slf4j
@Controller
public class GroupChatStompController {

    private final GroupOnlineUserService groupOnlineUserService;

    @MessageMapping("/user/join")
    public void join(@Payload GroupJoinDTO req, StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        Authentication auth = (Authentication) accessor.getUser();
        if (auth == null) {
            throw new SecurityException("인증되지 않은 연결 시도");
        }

        Long userNo = Long.parseLong(auth.getName());
        groupOnlineUserService.markOnline(sessionId, req.getGroupNo(), userNo, req.getNickname());
    }
}
