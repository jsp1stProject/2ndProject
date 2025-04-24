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
import com.sist.web.chat.group.vo.GroupVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
	
	private final GroupChatService chatService;
	
	@PostMapping
	public ResponseEntity<GroupVO> createGroup(@RequestBody GroupVO vo) {
		try {
			chatService.createGroup(vo);
		} catch (Exception ex) {
			log.info("그룹 생성 실패: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return ResponseEntity.ok(vo);
	}
	
	@GetMapping
	public ResponseEntity<List<GroupVO>> getGroupAll(HttpServletRequest request) {
		Long userNo = (Long)request.getAttribute("userno");
		List<GroupVO> list = new ArrayList<GroupVO>();
		
		try {
			list = chatService.getGroupAll(String.valueOf(userNo));
		} catch (Exception ex) {
			log.info("그룹 조회 실패: {}", ex.getMessage());
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		return ResponseEntity.ok(list);
	}
}
