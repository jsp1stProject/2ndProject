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
import org.apache.ibatis.annotations.Param;
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
	public void groupScheduleInsert(ScheduleVO vo) {
		dao.groupScheduleInsert(vo); 
	}

	@Override
	public void scheduleMemberInsert(ScheduleMemberVO vo) {
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
	    groupScheduleInsert(vo);
		System.out.println("인서트1");
		List<Long> participants = new ArrayList<Long>();
		
		//participants = vo.getParticipants();
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
		return dao.scheduleGroupList(group_no);
	}

	@Override
	public Map scheduleGroupListData(int group_no) {
		Map map = new HashMap();
		List<ScheduleVO> list = scheduleGroupList(group_no);
		map.put("list", list);
		return map;
	}

	@Override
	public List<ScheduleVO> scheduleUserTotalList(long user_no) {
		return dao.scheduleUserTotalList(user_no);
	}

	@Override
	public void scheduleInsert(ScheduleVO vo) {
		dao.scheduleInsert(vo);
	}

	@Override
	public ScheduleVO schedule_detail(int sche_no) {
		// TODO Auto-generated method stub
		ScheduleVO vo = new ScheduleVO();
		try {
			vo = dao.schedule_detail(sche_no);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vo;
	}

	@Override
	public List<ScheduleMemberVO> schedule_participatns(int shce_no) {
		// TODO Auto-generated method stub
		return dao.schedule_participatns(shce_no);
	}
	
	@Override
	public ScheduleVO scheduleDetailData(int shce_no) {
		ScheduleVO vo = new ScheduleVO();
		try {
			System.out.println("serviceimpl");
			vo = dao.schedule_detail(shce_no);
			System.out.println(vo);
			vo.setParticipants(dao.schedule_participatns(shce_no));
			System.out.println(vo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vo;
	}

	@Override
	public List<ScheduleVO> schedule_selected_data(@Param("selected_date") String selected_date,
            @Param("user_no") long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_selected_data(selected_date,user_no);
	}

	@Override
	public List<ScheduleVO> schedule_dday(long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_dday(user_no);
	}

	@Override
	public List<ScheduleVO> schedule_important(long user_no) {
		// TODO Auto-generated method stub
		return dao.schedule_important(user_no);
	}

 
}
