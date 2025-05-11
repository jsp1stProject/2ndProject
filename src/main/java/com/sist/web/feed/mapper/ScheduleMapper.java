package com.sist.web.feed.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleMapper {
	@Insert("INSERT INTO p_schedule(sche_no, group_no, user_no, type, sche_title, sche_content, "
			+ "sche_start,sche_end, regdate, is_public) " +
	        "VALUES (p_sche_no_seq.nextval, #{group_no}, #{user_no}, #{type}, #{sche_title},"
	        + " #{sche_content}, #{sche_start}, #{sche_end}, SYSDATE, #{is_public})")
	@SelectKey(statement = "SELECT p_sche_no_seq.currval FROM dual", keyProperty = "sche_no", before = false, resultType = int.class )
	public void scheduleInsert(ScheduleVO vo);
	
	@Insert("INSERT INTO p_schedule_member(sche_no, user_no) VALUES(#{sche_no},#{user_no})")
	public void scheduleMemberInsert(ScheduleMemberVO vo);
	
	@Select("SELECT sche_no, group_no, user_no, type, sche_title, sche_content, TO_CHAR(sche_start,'YYYY-MM-DD HH24:MI') as sche_start_str, "
			+ "TO_CHAR(sche_end,'YYYY-MM-DD HH24:MI') as sche_end_str, TO_CHAR(regdate,'YYYY-MM-DD') as dbday, is_public "
			+ "FROM p_schedule "
			+ "WHERE group_no=#{group_no} "
			+ "ORDER BY sche_start ASC ")
	public List<ScheduleVO> scheduleGroupList(int group_no);
}