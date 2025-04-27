package com.sist.web.feed.controller;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.sist.web.feed.service.*;
import com.sist.web.feed.vo.*;

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
