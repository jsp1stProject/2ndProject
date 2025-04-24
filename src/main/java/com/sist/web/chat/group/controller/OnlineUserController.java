package com.sist.web.chat.group.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.common.util.OnlineUserManager;

@RestController
@RequestMapping("/api/online")
public class OnlineUserController {
	@GetMapping("/users")
	public Set<String> getOnlineUsers() {
		return OnlineUserManager.getOnlineUsers();
	}
	
	@GetMapping("/count/users")
	public int getOnlineUsersCount() {
		return OnlineUserManager.getOnlineUserCount();
	}
}
