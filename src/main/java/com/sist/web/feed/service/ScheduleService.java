package com.sist.web.feed.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleService {
	public void groupScheduleInsert(ScheduleVO vo);
	public void scheduleMemberInsert(ScheduleMemberVO vo);
	public String scheduleInsertData(int group_no, ScheduleVO vo,long user_no);
	public List<ScheduleVO> scheduleGroupList(Map map);
	public List<ScheduleVO> scheduleGroupListData(long user_no, int group_no);
	public List<ScheduleVO> scheduleUserTotalList(long user_no);
	public void scheduleInsert(ScheduleVO vo);
	public ScheduleVO schedule_detail(int sche_no);
	public List<ScheduleMemberVO> schedule_participatns(int shce_no);
	public ScheduleVO scheduleDetailData(int shce_no);
	public List<ScheduleVO> schedule_selected_data(@Param("selected_date") String selected_date,
            @Param("user_no") long user_no);
	public List<ScheduleVO> schedule_dday(long user_no);
	public List<ScheduleVO> schedule_important(long user_no);
	
}
