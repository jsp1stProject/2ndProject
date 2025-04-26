package com.sist.web.feed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.feed.service.*;
import com.sist.web.feed.vo.*;
import java.util.*;

@RestController
public class GroupFeedRestController {
	
	@Autowired
	private GroupService service;
	
	@GetMapping("board/groups")
	public ResponseEntity<Map> group_list_vue()
	{
		Map map = new HashMap<>();
		try {
			List<GroupVO> list = service.groupListData();
			map.put("list", list);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("group_vue 완료");
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
}
