package com.sist.web.group.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupController {
	@GetMapping("/group/list")
	public String group_list(Model model)
	{
		model.addAttribute("main_jsp", "../group/list.jsp");
        return "main/main";
	}
	
	@GetMapping("/group/detail")
	public String group_detail()
	{
		return "group/detail";
	}
}
