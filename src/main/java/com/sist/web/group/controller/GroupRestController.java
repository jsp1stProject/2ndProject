package com.sist.web.group.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.service.GroupService;
import com.sist.web.group.dto.GroupMemberDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
	private final GroupService service;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> group_groups()
	{
		Map<String, Object> map = new HashMap<>();
		try {
			List<GroupDTO> list = service.getGroupAllList();
			map.put("list", list);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("======== error ========");
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		System.out.println("group_vue 완료");
		return ResponseEntity.ok(ApiResponse.success(map));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<GroupDTO>> createGroup(@Valid @RequestBody GroupDTO vo) {
		try {
			service.createGroup(vo);
		} catch (Exception ex) {
			log.info("그룹 생성 실패: {}", ex.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(vo));
	}
	
	@GetMapping("/{users}") // 계층 이동함 수정 예정
	public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupAll(HttpServletRequest request, @PathVariable Integer users) {
		Long userNo = (Long)request.getAttribute("userno");
		List<GroupDTO> list = new ArrayList<GroupDTO>();
		
		try {
			list = service.getGroupAll(String.valueOf(userNo));
		} catch (Exception ex) {
			log.info("그룹 조회 실패: {}", ex.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<GroupMemberDTO>>> getGroupMember(Integer groupNo) {
		List<GroupMemberDTO> list = service.getGroupMemberAllByGroupNo(groupNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@PostMapping("/{groupNo}/join")
	public ResponseEntity<ApiResponse<Map<String, Object>>> joinGroupRequests(HttpServletRequest request, int group_no)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		GroupJoinRequestsDTO dto = new GroupJoinRequestsDTO();
		try {
			Long userNo = (Long)request.getAttribute("userno");
			dto.setUser_no(userNo);
			dto.setGroup_no(group_no);
			service.insertJoinRequests(dto);
			map.put("userNo", userNo);
			map.put("groupNo", group_no);
			
		} catch (Exception e) {
			log.info("가입 신청 실패: {}", e.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(map));
	}
}
