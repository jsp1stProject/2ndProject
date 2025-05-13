package com.sist.web.feed.dao;

import java.util.List;

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
	
	public void scheduleInsert(ScheduleVO vo)
	{
		mapper.scheduleInsert(vo);
	}
	
	public void scheduleMemberInsert(ScheduleMemberVO vo)
	{
		mapper.scheduleMemberInsert(vo);
	}
	
	public List<ScheduleVO> scheduleGroupList(int group_no)
	{
		return mapper.scheduleGroupList(group_no);
	}
	
}
