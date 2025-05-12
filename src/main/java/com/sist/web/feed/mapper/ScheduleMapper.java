package com.sist.web.feed.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleMapper {
	@Insert("INSERT INTO p_schedule(sche_no, group_no, user_no, type, sche_title, sche_content, sche_start, sche_end, regdate, is_public) " +
	        "VALUES (p_sche_no_seq.nextval, #{group_no}, #{user_no}, #{type}, #{sche_title}, #{sche_content}, #{sche_start}, #{sche_end}, SYSDATE, #{is_public})")
	public void ScheduleInsert(ScheduleVO vo);
	
	@Insert("INSERT INTO p_schedule_member(sche_no, user_no) VALUES(#{sche_no},#{user_no})")
	public void ScheduleMemberInsert(ScheduleMemberVO vo);
}
