package com.sist.web.chat.group.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.chat.group.service.*;
import com.sist.web.chat.group.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GroupChatRestController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chats/groups/{groupNo}/messages")
	public ResponseEntity<List<GroupChatVO>> getLatestMessageByGroupId(@PathVariable int groupNo, @RequestParam(required = false) Long lastMessageNo) {
		List<GroupChatVO> messages = new ArrayList<GroupChatVO>();

		try {
			messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		} catch (Exception ex) {
			log.error("Latest Messages Error", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		return ResponseEntity.ok(messages);
	}
}
