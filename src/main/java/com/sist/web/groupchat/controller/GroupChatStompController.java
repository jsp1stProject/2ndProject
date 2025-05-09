package com.sist.web.groupchat.controller;

import com.sist.web.common.exception.code.GroupChatErrorCode;
import com.sist.web.common.exception.domain.GroupChatException;
import com.sist.web.groupchat.dto.GroupJoinDTO;
import com.sist.web.groupchat.service.GroupOnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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
    private final Validator validator;
    
    @MessageMapping("/user/join")
    public void join(@Payload GroupJoinDTO req, StompHeaderAccessor accessor) {
    	
    	Set<ConstraintViolation<GroupJoinDTO>> violations = validator.validate(req);
    	if (!violations.isEmpty()) {
    		String errorMsg = violations.stream()
    				.map(ConstraintViolation::getMessage)
    				.collect(Collectors.joining(","));
    		throw new GroupChatException(GroupChatErrorCode.INVALID_JOIN_REQUEST, errorMsg);
		}
    	
    	Authentication auth = (Authentication) accessor.getUser();
    	if (auth == null) {
			throw new GroupChatException(GroupChatErrorCode.UNAUTHORIZED);
		}
    	
    	Long userNo = Long.parseLong(auth.getName());
        String sessionId = accessor.getSessionId();
        
        groupOnlineUserService.markOnline(sessionId, req.getGroupNo(), userNo, req.getNickname());
    }
}
