package com.sist.web.feed.dao;

import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.web.feed.mapper.ScheduleMapper;
import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

@Repository
public class ScheduleDAO {
	@Autowired
	private ScheduleMapper mapper;
	
	public void ScheduleInsert(ScheduleVO vo)
	{
		mapper.ScheduleInsert(vo);
	}
	
	public void ScheduleMemberInsert(ScheduleMemberVO vo)
	{
		mapper.ScheduleMemberInsert(vo);
	}
}
