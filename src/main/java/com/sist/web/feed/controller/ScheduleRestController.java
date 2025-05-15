package com.sist.web.feed.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.feed.service.ScheduleService;
import com.sist.web.feed.vo.GroupVO;
import com.sist.web.feed.vo.ScheduleVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/schedules")
public class ScheduleRestController {
	
	private final ScheduleService service;
	
	@GetMapping("/{group_no}")
	public ResponseEntity<Map> schedule_group_list(int group_no, HttpServletRequest request)
	{
		Map map = new HashMap();
		try {
			
			map = service.scheduleGroupListData(group_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("schedule_group_list 완료");
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@PostMapping("{group_no}")
	public ResponseEntity<Map> schedule_insert(@ModelAttribute ScheduleVO vo,
											@RequestParam("group_no") int group_no,
											@RequestParam("participants") List<Long> participants,
											 HttpServletRequest request )
	{
		System.out.println("인서트!");
		System.out.println(vo.getSche_start_str());
		System.out.println(vo.getSche_end_str());
		Map map = new HashedMap();
		try {
			vo.setGroup_no(group_no);
			vo.setParticipants(participants);
			System.out.println(vo);
			long user_no = (long)request.getAttribute("userno");
			service.scheduleInsertData(group_no, vo, user_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("오류발생");
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> user_schedule_list (HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			long user_no = (long)request.getAttribute("userno");
			List<ScheduleVO> list = service.scheduleUserTotalList(16);
			map.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception.
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
}
