package com.sist.web.chat.group.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.chat.group.service.*;
import com.sist.web.chat.group.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GroupChatController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chat/group/chat.do")
	public String chat() {
		return "chat/group/chat";
	}
	
	@MessageMapping("/chats/groups/{groupId}")
	public void sendGroupChat(@DestinationVariable Long groupId, GroupChatVO vo) {
		String senderId = "user";
		System.out.println("사용자: " + senderId + " / 메시지: " + vo.getContent());
		
		chatService.saveAndSendGroupChatMessage(vo);
	}
}
