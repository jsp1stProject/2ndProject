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
    public void handleMessage(@Payload SitterChatMessageDTO dto,
                              Principal principal,
                              StompHeaderAccessor accessor) {

        // 1. 유효성 검증
        Set<ConstraintViolation<SitterChatMessageDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("유효성 검증 실패: " +
                violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
        }

        // 2. 인증 확인
        if (principal == null) throw new SecurityException("인증되지 않은 사용자");

        int senderNo = Integer.parseInt(principal.getName());
        dto.setSenderNo(senderNo); // ✨ 서버에서 신뢰성 있게 세팅

        // 3. DB 저장
        SitterChatVO chat = new SitterChatVO();
        chat.setRoom_no(dto.getRoomId());
        chat.setSender_no(senderNo);
        chat.setContent(dto.getChatContent());
        chat.setRead_flag("N"); // 기본값 처리

        service.insertChat(chat);

        // 4. 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/sub/chat/" + dto.getRoomId(), dto);
    }

}
