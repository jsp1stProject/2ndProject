package com.sist.web.chat.group.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.chat.group.service.*;
import com.sist.web.chat.group.vo.*;
import com.sist.web.common.exception.base.BaseCustomException;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class GroupChatRestController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chats/groups/{groupNo}/messages")
	public ResponseEntity<ApiResponse<List<GroupChatVO>>> getLatestMessageByGroupId(@PathVariable int groupNo, @RequestParam(required = false) Long lastMessageNo) {
		List<GroupChatVO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		return ResponseEntity.ok(ApiResponse.success(messages));
	}
	
	@GetMapping("/err/test")
	public ResponseEntity<ApiResponse<Object>> trigger() {
		throw new CommonException(CommonErrorCode.INVALID_INPUT);
	}
}
