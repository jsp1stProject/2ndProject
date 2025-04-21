package com.sist.web.chat.group.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.chat.websocket.config.JwtProvider;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final JwtProvider jwt;
	
	@GetMapping("/token")
	public String generateToken() {
		return jwt.generateToken("user");
	}
}
