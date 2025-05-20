package com.sist.web.groupchat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;
import com.sist.web.groupchat.service.GroupChatService;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/chats/groups")
@RequiredArgsConstructor
public class GroupChatRestController {
	
	private final GroupChatService chatService;
	
	@GetMapping("/{groupNo}/messages")
	public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getLatestMessageByGroupNo(
			@PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo, 
			@RequestParam(required = false) Long lastMessageNo) {
		List<GroupChatDTO> messages = chatService.getLatestMessageByGroupNo(groupNo, lastMessageNo);
		return ResponseEntity.ok(ApiResponse.success(messages)); 
	}
	
	@GetMapping("/{groupNo}/messages/search")
	public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getMessagesByFilters(
			@PathVariable Integer groupNo, 
			@ModelAttribute MessageSearchFilterDTO dto) {
		if (!dto.isValid()) {
			log.error("메세지 검색 유효성 검사 실패 sDate={}, eData={}", dto.getStartDate(), dto.getEndDate());
			throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
		}
		dto.setGroupNo(groupNo);
		
		List<GroupChatDTO> list = chatService.getMessagesByFilters(dto); 
		
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/{groupNo}/messages/around")
	public ResponseEntity<ApiResponse<List<GroupChatDTO>>> getMessagesAround(
			@PathVariable @Min(value = 1, message = "유효하지 않은 그룹 번호입니다") Integer groupNo,
			@RequestParam @Min(value = 1, message ="유효하지 않은 메세지 번호입니다") Integer messageNo) {
		List<GroupChatDTO> list = chatService.getMessagesAround(groupNo, messageNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
}
