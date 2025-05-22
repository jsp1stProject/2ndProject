package com.sist.web.sitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.sist.web.security.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sist.web.sitter.vo.*;
import com.sist.web.sitter.service.*;
@Controller
public class SitterController {
	@Autowired
	private SitterService service;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;


	@GetMapping("/sitter/list")
	public String sitter(Model model)
	{
		model.addAttribute("main_jsp", "../sitter/list.jsp");
        return "main/main";
	}
	
	@GetMapping("/sitter/detail")
	public String sitterDetail(
	        @RequestParam int sitter_no,
	        @CookieValue(name = "accessToken", required = false) String token,
	        Model model) {

	    // 토큰 유효성 검사
	    if (token == null || token.isEmpty()) {
	        return "redirect:/login";
	    }

	    int user_no;
	    try {
	        user_no = Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
	    } catch (Exception e) {
	        return "redirect:/user/login";
	    }

	    SitterVO vo = service.sitterDetailData(sitter_no);
	    model.addAttribute("vo", vo);
	    model.addAttribute("user_no", user_no); 
	    model.addAttribute("main_jsp", "../sitter/detail.jsp");
	    return "main/main";
	}

	
	@GetMapping("/sitter/insert")
    public String sitter_insert(Model model) 
	{
		model.addAttribute("main_jsp", "../sitter/insert.jsp");
		return "main/main";
    }

	@GetMapping("/sitter/update")
	public String sitter_update(Model model) 
	{
		model.addAttribute("main_jsp", "../sitter/update.jsp");
		return "main/main";
	}
}