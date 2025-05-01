package com.sist.web.groupchat.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.groupchat.service.GroupOnlineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Slf4j
public class OnlineUserController {
	
	private final GroupOnlineUserService onlineUserService;
	
	@GetMapping("/online")
	public ResponseEntity<ApiResponse<List<Long>>> getOnlineUsers() {
		Set<Long> onlineUsers = onlineUserService.getOnlineUsers();
		System.out.println(Arrays.asList(onlineUsers));
		return ResponseEntity.ok(ApiResponse.success(new ArrayList<>(onlineUsers)));
	}
	
}
