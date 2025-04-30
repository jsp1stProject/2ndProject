package com.sist.web.groupchat.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.service.GroupChatService;
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
	public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getLatestMessageByGroupNo(@PathVariable Integer groupNo, @RequestParam(required = false) Long lastMessageNo) {
		if (groupNo == null) {
			log.warn("groupNo 누락: {}", groupNo);
			throw new CommonException(CommonErrorCode.MISSING_PARAMETER);
		}
		List<GroupChatDTO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		return ResponseEntity.ok(ApiResponse.success(messages));
	}
	
	@GetMapping("/err/test")
	public ResponseEntity<ApiResponse<Object>> trigger() {
		throw new CommonException(CommonErrorCode.INVALID_INPUT);
	}
}
