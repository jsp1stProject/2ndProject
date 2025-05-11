package com.sist.web.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.feed.service.GroupFeedService;
import com.sist.web.feed.service.ScheduleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ScheduleController {
	
	private final ScheduleService service;
	
	@GetMapping("/schedule/home")
	public String schedule_home()
	{
		
		return "schedule/schedule_home";
	}
}
