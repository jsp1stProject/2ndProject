package com.sist.web.chat.group.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sist.web.chat.group.service.*;
import com.sist.web.chat.group.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GroupChatController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chat/group/chat")
	public String chat() {
		return "chat/group/chat";
	}
	
	@MessageMapping("/chats/groups/{groupNo}")
	public void sendGroupChat(@DestinationVariable int groupNo, GroupChatVO vo, Principal principal) {
		int senderNo = Integer.parseInt(principal.getName());
		vo.setSender_no(senderNo);
		vo.setGroup_no(groupNo);
		
		chatService.saveAndSendGroupChatMessage(vo);
	}
}
