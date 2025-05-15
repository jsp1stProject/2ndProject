package com.sist.web.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.feed.service.GroupFeedService;
import com.sist.web.feed.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ScheduleController {
	
	private final ScheduleService service;
	
	@GetMapping("/schedule/home")
	public String schedule_home(Model model)
	{
		model.addAttribute("main_jsp","../schedule/schedule_home.jsp");
		return "main/main";
	}
}
