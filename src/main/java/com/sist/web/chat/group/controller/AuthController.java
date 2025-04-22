package com.sist.web.chat.group.controller;

import java.util.HashMap;
import java.util.Map;

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
	public Map<String, String> generateTokenn() {
		String userId = "user"; // ������ ���� or ���� ���ؽ�Ʈ���� �����;� ��
		String token = jwt.generateToken(userId);
		Map<String, String> result = new HashMap<String, String>();
		result.put("token", token);
		result.put("userId", userId);
		return result;
	}
}
