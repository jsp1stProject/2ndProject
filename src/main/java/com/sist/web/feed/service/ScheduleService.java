package com.sist.web.feed.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleService {
	public void scheduleInsert(ScheduleVO vo);
	public void scheduleMemberInsert(ScheduleMemberVO vo);
	public Map scheduleInsertData(int group_no, ScheduleVO vo,long user_no);
	public List<ScheduleVO> scheduleGroupList(int group_no);
	public Map scheduleGroupListData(int group_no);
}
