package com.sist.web.groupchat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.service.GroupChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GroupChatController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/groupchat/chat")
	public String chat() {
		return "groupchat/chat";
	}
	
	@MessageMapping("/chats/groups/{groupNo}")
	public void sendGroupChat(@DestinationVariable int groupNo, GroupChatDTO vo, Principal principal) {
		int senderNo = Integer.parseInt(principal.getName());
		vo.setSender_no(senderNo);
		vo.setGroup_no(groupNo);
		
		chatService.saveAndSendGroupChatMessage(vo);
	}
}
