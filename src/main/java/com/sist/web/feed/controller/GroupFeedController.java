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
	/*
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
	*/
	@GetMapping("/group/feed")
	public String group_feed(int feed_no, Model model, HttpServletRequest request)
	{
		FeedVO vo = service.feedData(feed_no);
		long user_no = (long)request.getAttribute("userno");
		
		System.out.println("vo는 "+vo);
		System.out.println("user_no는"+user_no);
		
		model.addAttribute("user_no", user_no);
		model.addAttribute("vo",vo);
		return "group/feed";
	}
	
}
