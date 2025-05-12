package com.sist.web.group.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {
	@GetMapping("/group/list")
	public String group_list()
	{
		return "group/list";
	}
	
	@GetMapping("/group/detail")
	public String group_detail()
	{
		return "group/detail";
	}
}
