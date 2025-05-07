package com.sist.web.chat.group.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.chat.group.service.GroupChatService;
import com.sist.web.chat.group.vo.GroupMemberVO;
import com.sist.web.chat.group.vo.GroupVO;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
	
	private final GroupChatService chatService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<GroupVO>> createGroup(@RequestBody GroupVO vo) {
		try {
			chatService.createGroup(vo);
		} catch (Exception ex) {
			log.info("그룹 생성 실패: {}", ex.getMessage());
//			throw new CommonException(CommonErrorCode.INTERNAL_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(vo));
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<GroupVO>>> getGroupAll(HttpServletRequest request) {
		Long userNo = (Long)request.getAttribute("userno");
		List<GroupVO> list = new ArrayList<GroupVO>();
		
		try {
			list = chatService.getGroupAll(String.valueOf(userNo));
		} catch (Exception ex) {
			log.info("그룹 조회 실패: {}", ex.getMessage());
//			throw new CommonException(CommonErrorCode.INTERNAL_ERROR);
		}
		
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<GroupMemberVO>>> getGroupMember(int groupNo) {
		List<GroupMemberVO> list = chatService.getGroupMemberAllByGroupNo(groupNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
}
