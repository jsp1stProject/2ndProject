package com.sist.web.feed.service;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleService {
	public void ScheduleInsert(ScheduleVO vo);
	public void ScheduleMemberInsert(ScheduleMemberVO vo);
	
}
