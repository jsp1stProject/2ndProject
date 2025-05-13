package com.sist.web.feed.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.feed.dao.ScheduleDAO;
import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleDAO dao;
		
	@Override
	public void scheduleInsert(ScheduleVO vo) {
		// TODO Auto-generated method stub
		dao.scheduleInsert(vo);		
	}

	@Override
	public void scheduleMemberInsert(ScheduleMemberVO vo) {
		// TODO Auto-generated method stub
		dao.scheduleMemberInsert(vo);
	}
	
	@Override
	public Map scheduleInsertData(int group_no, ScheduleVO vo, long user_no)
	{
		System.out.println("서비스임플");	
		Map map = new HashedMap();
		vo.getSche_start_str();
	    vo.getSche_end_str();
	    
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		if (vo.getSche_start_str() != null && !vo.getSche_start_str().isEmpty())
		{
		    LocalDateTime start = LocalDateTime.parse(vo.getSche_start_str(), formatter);
		    vo.setSche_start(Timestamp.valueOf(start));
		}

		if (vo.getSche_end_str() != null && !vo.getSche_end_str().isEmpty())
		{
		    LocalDateTime end = LocalDateTime.parse(vo.getSche_end_str(), formatter);
		    vo.setSche_end(Timestamp.valueOf(end));
		}
		vo.setUser_no(user_no);
	    System.out.println(vo);
	    scheduleInsert(vo);
		System.out.println("인서트1");
		List<Long> participants = new ArrayList<Long>();
		
		participants = vo.getParticipants();
		for(long participant : participants)
		{
			ScheduleMemberVO mvo = new ScheduleMemberVO();
			mvo.setSche_no(vo.getSche_no());
			mvo.setUser_no(participant);
			dao.scheduleMemberInsert(mvo);
			System.out.println("인서트2");
		}
		scheduleGroupListData(group_no);
		System.out.println("인서트3");
		return map;
	}


	@Override
	public List<ScheduleVO> scheduleGroupList(int group_no) {
		// TODO Auto-generated method stub
		return dao.scheduleGroupList(group_no);
	}

	@Override
	public Map scheduleGroupListData(int group_no) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		List<ScheduleVO> list = scheduleGroupList(group_no);
		map.put("list", list);
		return map;
	}

	@Override
	public List<ScheduleVO> scheduleUserTotalList(long user_no) {
		// TODO Auto-generated method stub
		return dao.scheduleUserTotalList(user_no);
	}
	

}
