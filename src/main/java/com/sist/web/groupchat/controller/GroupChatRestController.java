package com.sist.web.groupchat.controller;

import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.service.GroupChatService;
import com.sist.web.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupChatRestController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/chats/groups/{groupNo}/messages")
	public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getLatestMessageByGroupNo(
			@PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo, 
			@RequestParam(required = false) Long lastMessageNo) {
		List<GroupChatDTO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		return ResponseEntity.ok(ApiResponse.success(messages));
	}
}
