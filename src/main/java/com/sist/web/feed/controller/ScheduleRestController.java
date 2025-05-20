package com.sist.web.feed.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.feed.service.ScheduleService;
import com.sist.web.feed.vo.GroupVO;
import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/schedules")
public class ScheduleRestController {
	
	private final ScheduleService service;
	
	@GetMapping("/group/{group_no}")
	public ResponseEntity<List<ScheduleVO>> group_schedule_list(@PathVariable("group_no") int group_no, HttpServletRequest request)
	{
		List<ScheduleVO> schedule_list = new ArrayList<ScheduleVO>();
		try {
			long user_no = (long)request.getAttribute("userno");
			schedule_list = service.scheduleGroupListData(user_no,group_no);
			System.out.println("리스트 길이"+schedule_list.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("schedule_group_list 완료");
		return new ResponseEntity<>(schedule_list,HttpStatus.OK);
	}
	
	@PostMapping("/group/{group_no}")
	public ResponseEntity<String> group_schedule_insert(@PathVariable("group_no") int group_no, @ModelAttribute ScheduleVO vo,
											 HttpServletRequest request )
	{
		System.out.println("인서트!");
		System.out.println(vo.getSche_start_str());
		System.out.println(vo.getSche_end_str());
		System.out.println("vo값은 "+vo);
		String result="";

		try {
			//vo.setParticipants(participants);
			System.out.println(vo);
			long user_no = (long)request.getAttribute("userno");
			result = service.scheduleInsertData(vo.getGroup_no(), vo, user_no);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("종료");
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<Map<String, Object>> user_schedule_list (HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			long user_no = (long)request.getAttribute("userno");
			List<ScheduleVO> list = service.scheduleUserTotalList(user_no);
			List<ScheduleVO> ddayList = service.schedule_dday(user_no);
			List<ScheduleVO> importList = service.schedule_important(user_no);
			map.put("list", list);
			map.put("ddayList", ddayList);
			map.put("importList", importList);
		} catch (Exception e) {
			// TODO: handle exception.
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<String> schedule_insert(HttpServletRequest request, @ModelAttribute ScheduleVO vo)
	{
		System.out.println("스케줄 insert restController 진입");
		String result="";
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			if(vo.getSche_start_str()!=null)
			{
				LocalDateTime start = LocalDateTime.parse(vo.getSche_start_str(), formatter);
				vo.setSche_start(Timestamp.valueOf(start));
			}
			if(vo.getSche_end_str()!=null)
			{
				LocalDateTime end = LocalDateTime.parse(vo.getSche_end_str(), formatter);
				vo.setSche_end(Timestamp.valueOf(end));
			}
			long user_no = (long)request.getAttribute("userno");
			vo.setUser_no(user_no);
			
			System.out.println("########## vo #########");
			System.out.println(vo);
			service.scheduleInsert(vo);
			result="일정 등록 완료";
		}
		catch (Exception e) {
			// TODO: handle exception
			result="일정 등록 실패";
			return new ResponseEntity<String>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(result,HttpStatus.OK);
	}
	
	@GetMapping("/{sche_no}/detail")
	public ResponseEntity<ScheduleVO> schedule_detail(@PathVariable("sche_no") int sche_no)
	{
		ScheduleVO vo = new ScheduleVO();
		try {
			vo = service.scheduleDetailData(sche_no);
			System.out.println("vo데이터");
			System.out.println(vo);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<ScheduleVO>(vo,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ScheduleVO>(vo,HttpStatus.OK);
	}
	
	@GetMapping("/date/{selected_date}")
	public ResponseEntity<List<ScheduleVO>> schedule_seleted_list(@PathVariable("selected_date") String selected_date,
			HttpServletRequest request)
	{
		System.out.println("선택된날짜");
		List<ScheduleVO> list = new ArrayList<ScheduleVO>();
		try {
			long user_no = (long)request.getAttribute("userno");
			list = service.schedule_selected_data(selected_date,user_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<List<ScheduleVO>>(list,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ScheduleVO>>(list, HttpStatus.OK);
	}
	
}
