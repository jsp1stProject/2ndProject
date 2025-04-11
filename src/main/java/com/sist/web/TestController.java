package com.sist.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.service.TestService;
import com.sist.vo.TestVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestController {
	private final TestService testService;
	
	@GetMapping("main.do")
	public String main(Model model) {
		TestVO vo=testService.GetName();
		model.addAttribute("test",vo);
		return "main/home";
	}
}
