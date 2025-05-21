package com.sist.web.feed.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.web.feed.mapper.ScheduleMapper;
import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

@Repository
public class ScheduleDAO {
	@Autowired
	private ScheduleMapper mapper;
	
	public void groupScheduleInsert(ScheduleVO vo)
	{
		mapper.groupScheduleInsert(vo);
	}
	
	public void scheduleMemberInsert(ScheduleMemberVO vo)
	{
		mapper.scheduleMemberInsert(vo);
	}
	
	public List<ScheduleVO> scheduleGroupList(Map map)
	{
		return mapper.scheduleGroupList(map);
	}
	
	public List<ScheduleVO> scheduleUserTotalList(long user_no)
	{
		return mapper.scheduleUserTotalList(user_no);
	}
	
	public void scheduleInsert(ScheduleVO vo)
	{
		mapper.scheduleInsert(vo);
	}
	
	public ScheduleVO schedule_detail(int sche_no)
	{
		return mapper.schedule_detail(sche_no);
	}
	
	public List<ScheduleMemberVO> schedule_participatns(int shce_no)
	{
		return mapper.schedule_participatns(shce_no);
	}
	
	public List<ScheduleVO> schedule_selected_data(@Param("selected_date") String selected_date,
            @Param("user_no") long user_no)
	{
		return mapper.schedule_selected_data(selected_date,user_no); 
	}
	
	public List<ScheduleVO> schedule_dday(long user_no)
	{
		return mapper.schedule_dday(user_no);
	}
	
	public List<ScheduleVO> schedule_important(long user_no)
	{
		return mapper.schedule_important(user_no);
	}
	public void deleteSchedule(int sche_no)
	{
		mapper.deleteSchedule(sche_no);
	}
	
	public void deleteScheduleMember(int sche_no)
	{
		mapper.deleteScheduleMember(sche_no);
	}
	
	public void updateSchedule(ScheduleVO vo)
	{
		mapper.updateSchedule(vo);
	}
}
