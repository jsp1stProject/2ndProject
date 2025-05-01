package com.sist.web.group.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.service.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupeRestController {
	private final GroupService service;
	
	@GetMapping()
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
	/*
	@GetMapping("/{userNo}") // api/groups/userNo
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
	*/
}
