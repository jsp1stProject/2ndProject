package com.sist.web.feed.service;

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
	public void ScheduleInsert(ScheduleVO vo) {
		// TODO Auto-generated method stub
		dao.ScheduleInsert(vo);		
	}

	@Override
	public void ScheduleMemberInsert(ScheduleMemberVO vo) {
		// TODO Auto-generated method stub
		dao.ScheduleMemberInsert(vo);
	}

}
