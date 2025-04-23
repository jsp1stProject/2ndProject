package com.sist.web.chat.group.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.chat.group.service.*;
import com.sist.web.chat.group.vo.*;
import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.CommonErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GroupChatRestController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chats/groups/{groupNo}/messages")
	public ResponseEntity<List<GroupChatVO>> getLatestMessageByGroupId(@PathVariable int groupNo, @RequestParam(required = false) Long lastMessageNo) {
		List<GroupChatVO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		return ResponseEntity.ok(messages);
	}
	
	@GetMapping("/err/test")
	public void trigger() {
		throw new BaseCustomException(CommonErrorCode.INVALID_INPUT) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		};
	}
}
