package com.sist.web.sitterchat.controller;

import com.sist.web.sitterchat.dto.SitterChatMessageDTO;
import com.sist.web.sitterchat.service.SitterChatService;
import com.sist.web.sitterchat.vo.SitterChatVO;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SitterChatStompController {

    private final SitterChatService service;
    private final SimpMessagingTemplate messagingTemplate;
    private final Validator validator;

    @MessageMapping("/chatSend")
    public void handleMessage(
            @Payload SitterChatMessageDTO dto,
            Principal principal,
            StompHeaderAccessor accessor) {

        // 유효성 검증
        Set<ConstraintViolation<SitterChatMessageDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
            throw new IllegalArgumentException("유효성 검증 실패: " + errorMsg);
        }

        // 사용자 인증 
        if (principal == null) {
            throw new SecurityException("인증되지 않은 사용자");
        }
        int senderNo = Integer.parseInt(principal.getName());

       
        SitterChatVO chat = new SitterChatVO();
        chat.setRoom_id(dto.getRoomId());
        chat.setSender_no(senderNo);
        chat.setReceiver_no(dto.getReceiverNo());
        chat.setChat_content(dto.getChatContent());
        chat.setChat_type(dto.getChatType());

        service.SitterChatInsert(chat);

        messagingTemplate.convertAndSend("/ssub/chat/" + dto.getRoomId(), dto);
    }
}
