package com.sist.web.groupchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorTestController {
	@GetMapping("/err/test/view")
	public String errTest() {
		throw new RuntimeException("강제 예외 발생");
	}
}
