package com.sist.web.feed.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.feed.service.*;
import com.sist.web.feed.vo.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class GroupFeedController {
	
	private final GroupFeedService service;

	@GetMapping("/group/feed")
	public String group_feed(Model model)
	{
		model.addAttribute("main_jsp","../group/feed.jsp");
		return "main/main";
	}
	
	@GetMapping("/schedule/list")
	public String schedule_list(Model model)
	{
		model.addAttribute("main_jsp","../schedule/schedule_list.jsp");
		return "main/main";
	}
	
}
