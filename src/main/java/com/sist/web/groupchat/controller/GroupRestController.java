package com.sist.web.groupchat.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.groupchat.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;
import com.sist.web.groupchat.service.GroupChatService;
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
	public ResponseEntity<ApiResponse<GroupDTO>> createGroup(@Valid @RequestBody GroupDTO vo) {
		try {
			chatService.createGroup(vo);
		} catch (Exception ex) {
			log.info("그룹 생성 실패: {}", ex.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(vo));
	}
	// /api/groups 그룹 생성
	// /api/groups/users 사용자 가입 그룹
	// /api/groups 전체 그룹
	// /api/groups/feeds
	@GetMapping() // 계층 이동함 수정 예정
	public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupAll(HttpServletRequest request) {
		Long userNo = (Long)request.getAttribute("userno");
		List<GroupDTO> list = new ArrayList<GroupDTO>();
		
		try {
			list = chatService.getGroupAll(String.valueOf(userNo));
		} catch (Exception ex) {
			log.info("그룹 조회 실패: {}", ex.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<GroupMemberDTO>>> getGroupMember(Integer groupNo) {
		List<GroupMemberDTO> list = chatService.getGroupMemberAllByGroupNo(groupNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
}
