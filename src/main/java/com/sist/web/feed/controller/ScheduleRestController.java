package com.sist.web.feed.controller;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.feed.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleRestController {
	@Autowired
	private ScheduleService service;
	
	@PostMapping("")
	public ResponseEntity<Map>schedule_insert()
	{
		Map map = new HashedMap();
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
}
