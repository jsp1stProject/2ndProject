package com.sist.web.chat.group.controller;

import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.chat.group.service.OnlineUserService;
import com.sist.web.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Slf4j
public class OnlineUserController {
	
	private final OnlineUserService onlineUserService;
	
	@GetMapping("/online")
	public ResponseEntity<ApiResponse<Set<Long>>> getOnlineUsers() {
		
		return ResponseEntity.ok(ApiResponse.success(onlineUserService.getOnlineUsers()));
	}
	
}
