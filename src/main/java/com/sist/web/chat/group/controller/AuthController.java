package com.sist.web.chat.group.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final JwtTokenProvider jwt; 
	
	@GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> generateToken(HttpServletRequest request) {
		Long userNo = (Long)request.getAttribute("userno");
		String nickname = (String)request.getAttribute("nickname");
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>)request.getAttribute("role");
		
		String token = 
				jwt.createToken(String.valueOf(userNo), 
						 roles.stream()
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList()));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("token", token);
		result.put("userNo", userNo);
		result.put("nickname", nickname);
		
		return result;
	}
}
