package com.sist.web.group.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.service.GroupService;
import com.sist.web.group.dto.GroupMemberDTO;
import com.sist.web.group.dto.GroupMemberInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
	private final GroupService service;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> group_groups(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<>();
		try {
			Long userNo = (Long)request.getAttribute("userno");
			int user_no = userNo.intValue();
			System.out.println(user_no);
			map = service.getGroupListAndStates(user_no);
			System.out.println(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("======== error ========");
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		System.out.println("group_vue 완료");
		return ResponseEntity.ok(ApiResponse.success(map));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<GroupDTO>> createGroup(
			@Valid @RequestPart("groupDetail") GroupDTO dto, 
			@RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
		service.createGroup(dto, profileImg);
		return ResponseEntity.ok(ApiResponse.success(dto));
	}
	
	@GetMapping("/{users}")
	public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupAll(HttpServletRequest request, @PathVariable Integer users) {
		Long userNo = (Long)request.getAttribute("userno");
		if (userNo == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		List<GroupDTO> list = service.getGroupAll(String.valueOf(userNo));
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/{groupNo}/members")
	public ResponseEntity<ApiResponse<List<GroupMemberDTO>>> getGroupMemberAllByGroupNo(@PathVariable Integer groupNo) {
		if (groupNo == null) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		List<GroupMemberDTO> list = service.getGroupMemberAllByGroupNo(groupNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@PostMapping("/members")
	public ResponseEntity<ApiResponse<GroupMemberDTO>> addGroupMember(@Valid @RequestBody GroupMemberDTO dto) {
		service.addGroupMember(dto);
		return ResponseEntity.ok(ApiResponse.success(dto, "그룹 멤버 추가 성공"));
	}
	
	@PostMapping("/{groupNo}/join")
	public ResponseEntity<ApiResponse<Map<String, Object>>> joinGroupRequests(@PathVariable("groupNo") Integer groupNo, HttpServletRequest request)
	{
		System.out.println("가입포스트매핑");
		Map<String, Object> map = new HashMap<String, Object>();
		GroupJoinRequestsDTO dto = new GroupJoinRequestsDTO();
		try {
			Long userNo = (Long)request.getAttribute("userno");
			dto.setUser_no(userNo);
			dto.setGroup_no(groupNo);
			service.insertJoinRequests(dto);
			map.put("userNo", userNo);
			map.put("groupNo", groupNo);
			
		} catch (Exception e) {
			log.info("가입 신청 실패: {}", e.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(map));
	}
	
	@GetMapping("/{groupNo}/detail")
	public ResponseEntity<ApiResponse<GroupDTO>> getGroupDetailByGroupNo(@PathVariable Integer groupNo) {
		if (groupNo == null) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		System.out.println(service.getGroupDetailByGroupNo(groupNo).toString());
		return ResponseEntity.ok(ApiResponse.success(service.getGroupDetailByGroupNo(groupNo)));
	}
	
	@PutMapping(value = "/{groupNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateGroupDetail(
    		@PathVariable("groupNo") Integer groupNo,
    		@RequestPart("groupDetail") GroupDTO dto,
    		@RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {
		dto.setGroup_no(groupNo);
        service.updateGroupDetail(dto, profileImg, dto.getTags());
		return ResponseEntity.ok(ApiResponse.success("그룹 정보가 수정되었습니다."));
    }
	
	@GetMapping("/{userNo}/join_requests")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getGroupJoinRequests(@PathVariable Integer userNo)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		if (userNo == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		List<GroupJoinRequestsDTO> reqlist = service.selectGroupRequestsData(userNo);
		map.put("reqlist", reqlist);
		return ResponseEntity.ok(ApiResponse.success(map));
		
	}
	
	@PostMapping("/{requestNo}/join_requests_result")
	public ResponseEntity<ApiResponse<String>> join_requests_result(@PathVariable Integer requestNo, @RequestBody GroupJoinRequestsDTO dto)
	{
		String status = dto.getStatus();
		String nickname = dto.getUser_nickname();
		int request_no = requestNo;
		int group_no = dto.getGroup_no();
		long user_no = dto.getUser_no();
		System.out.println(status);
		System.out.println(nickname);
		System.out.println(request_no);
		System.out.println(group_no);
		System.out.println(user_no);
		service.joinRequestResult(request_no, group_no, user_no, status, nickname);
		
		return ResponseEntity.ok(ApiResponse.success("심사후 멤버추가 완료"));
	}
	
	@DeleteMapping("/{groupNo}")
	public ResponseEntity<ApiResponse<Object>> deleteGroup(@PathVariable Integer groupNo, HttpServletRequest req) {
		int userNo = (Integer) req.getAttribute("userno");
		service.removeGroup(groupNo, userNo);
		return ResponseEntity.ok(ApiResponse.success(null, "그룹이 삭제되었습니다."));
	}
	
	@GetMapping("/members/details")
	public ResponseEntity<ApiResponse<GroupMemberInfoDTO>> getGroupMemberDetail(Integer groupNo, Integer userNo) {
		// 유효성 검사 필요
		GroupMemberInfoDTO dto = service.getGroupMemberDetail(groupNo, userNo);
		return ResponseEntity.ok(ApiResponse.success(dto));
	}
}
