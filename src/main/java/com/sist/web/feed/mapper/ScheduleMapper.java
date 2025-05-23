package com.sist.web.feed.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.sist.web.feed.vo.ScheduleMemberVO;
import com.sist.web.feed.vo.ScheduleVO;

public interface ScheduleMapper {
	//그룹페이지 내 그룹일정
	@Insert("INSERT INTO p_schedule(sche_no, group_no, user_no, type, sche_title, sche_content, "
			+ "sche_start,sche_end, is_public, is_important, alarm) " +
	        "VALUES (p_sche_no_seq.nextval, #{group_no}, #{user_no}, #{type}, #{sche_title},"
	        + " #{sche_content}, #{sche_start}, #{sche_end}, #{is_public}, #{is_important}, #{alarm})")
	@SelectKey(statement = "SELECT p_sche_no_seq.currval FROM dual", keyProperty = "sche_no", before = false, resultType = int.class )
	public void groupScheduleInsert(ScheduleVO vo);
	
	@Insert("INSERT INTO p_schedule_member(sche_no, user_no) VALUES(#{sche_no},#{user_no})")
	public void scheduleMemberInsert(ScheduleMemberVO vo);
	
	public List<ScheduleVO> scheduleGroupList(Map map);

	/*
	 * <select id="scheduleParticipants" parameterType="list"
	 * resultType="com.sist.web.user.vo.UserVO"> public List<userV>
	 */
	//일정관리페이지
	@Select("SELECT DISTINCT p.sche_no, p.sche_title,TO_CHAR(p.sche_start, 'YYYY-MM-DD HH24:MI') as sche_start_str, "
			+ "TO_CHAR(p.sche_end, 'YYYY-MM-DD HH24:MI') as sche_end_str, p.is_important, p.type "
			+ "FROM p_schedule p "
			+ "LEFT JOIN p_schedule_member m "
			+ "ON p.sche_no=m.sche_no "
			+ "WHERE m.user_no=#{user_no} OR p.user_no=#{user_no}")
	public List<ScheduleVO> scheduleUserTotalList(long user_no);
	
	@Select("SELECT * "
			+ "FROM (SELECT a.*, ROWNUM AS num "
			+ "FROM (SELECT p.sche_no, p.sche_title, "
			+ "TO_CHAR(p.sche_start, 'YYYY-MM-DD HH24:MI') AS sche_start_str,"
			+ "TO_CHAR(p.sche_end, 'YYYY-MM-DD HH24:MI') AS sche_end_str, "
			+ "p.is_important, p.type "
			+ "FROM p_schedule p "
			+ "LEFT JOIN p_schedule_member m ON p.sche_no = m.sche_no "
			+ "WHERE m.user_no = #{user_no} OR p.user_no = #{user_no} "
			+ "GROUP BY "
			+ "p.sche_no, p.sche_title, p.sche_start, p.sche_end, p.is_important, p.type "
			+ "ORDER BY p.sche_start DESC) a "
			+ "WHERE ROWNUM <= #{end} ) "
			+ "WHERE num > #{start}")
	public List<ScheduleVO> schedulePagingUserTotalList(Map map);
	
	@Select("SELECT CEIL(COUNT(DISTINCT p.sche_no)/15.0)"
			+ "FROM p_schedule p LEFT JOIN p_schedule_member m ON p.sche_no = m.sche_no "
			+ "WHERE m.user_no = #{user_no} OR p.user_no = #{user_no}")
	public int scheduleUserTotalCount(long user_no);
	

	public List<ScheduleVO> schedulePagingUserSearchlList(Map map);
	
	@Select("SELECT CEIL(COUNT(DISTINCT p.sche_no)/15.0) "
			+ "FROM p_schedule p LEFT JOIN p_schedule_member m ON p.sche_no = m.sche_no "
			+ "WHERE (m.user_no = #{user_no} OR p.user_no = #{user_no}) "
			+ "AND p.sche_title LIKE '%' || #{search} || '%'")
	public int scheduleUserTotalCountWithSearch(@Param("user_no") long user_no, @Param("search") String search);
	
	@Insert("INSERT INTO p_schedule(sche_no, group_no, user_no, sche_title, sche_content, sche_start, sche_end, is_important, alarm ) "
			+"VALUES(p_sche_no_seq.nextval, null, #{user_no}, #{sche_title}, #{sche_content}, #{sche_start}, #{sche_end}, "
			+"#{is_important}, #{alarm})")
	public void scheduleInsert(ScheduleVO vo);
	
	@Select("SELECT sche_no, group_no, user_no, type, sche_title, sche_content, "
			+ "TO_CHAR(sche_start,'YYYY-MM-DD HH24:MI') as sche_start_str, "
			+ "TO_CHAR(sche_end,'YYYY-MM-DD HH24:MI') as sche_end_str, "
			+ "is_public, is_important, alarm "
			+ "FROM p_schedule "
			+ "WHERE sche_no = #{sche_no}")
	public ScheduleVO schedule_detail(int sche_no);
	
	@Select("SELECT u.user_no, u.nickname "
			+ "FROM p_schedule_member m "
			+ "JOIN p_users u ON m.user_no=u.user_no "
			+ "WHERE m.sche_no=#{sche_no}")
	public List<ScheduleMemberVO> schedule_participatns(int shce_no);
	
	@Select("SELECT DISTINCT p.sche_no, p.sche_title, "
			+ "TO_CHAR(p.sche_start,'YYYY-MM-DD') as sche_start_str, "
			+ "TO_CHAR(p.sche_end,'YYYY-MM-DD') as sche_end_str, "
			+ "p.is_important,p.type "
			+ "FROM p_schedule p "
			+ "LEFT JOIN p_schedule_member m "
			+ "ON p.sche_no=m.sche_no "
			+ "WHERE TO_CHAR(p.sche_start, 'YYYY-MM-DD') <= #{selected_date} AND "
			+ "TO_CHAR(p.sche_end, 'YYYY-MM-DD') >= #{selected_date} "
			+ "AND (p.user_no=#{user_no} OR m.user_no=#{user_no}) "
			+ "ORDER BY sche_start_str ASC")
	public List<ScheduleVO> schedule_selected_data(@Param("selected_date") String selected_date,
            @Param("user_no") long user_no);
	
	public List<ScheduleVO> schedule_dday(long user_no);
	
	@Select("SELECT * "
			+ "FROM (SELECT DISTINCT p.sche_no, p.sche_title, "
			+ "TO_CHAR(p.sche_start,'YYYY-MM-DD') as sche_start_str, "
			+ "TO_CHAR(p.sche_end,'YYYY-MM-DD') as sche_end_str, p.type "
			+ "FROM p_schedule p "
			+ "LEFT JOIN p_schedule_member m "
			+ "ON p.sche_no=m.sche_no "
			+ "WHERE TRUNC(p.sche_end) >= TRUNC(SYSDATE) "
			+ "AND (p.user_no=#{user_no} OR m.user_no=#{user_no}) "
			+ "AND p.is_important=1"
			+ "ORDER BY sche_start_str ASC) "
			+ "WHERE rownum <=5")
	public List<ScheduleVO> schedule_important(long user_no);
			
	@Delete("DELETE FROM p_schedule WHERE sche_no = #{sche_no}")
	public void deleteSchedule(int sche_no);
	
	@Delete("DELETE FROM p_schedule_member WHERE sche_no = #{sche_no}")
	public void deleteScheduleMember(int sche_no);

	@Update("UPDATE p_schedule SET sche_title = #{sche_title},sche_content = #{sche_content},"
			+ "sche_start = #{sche_start},sche_end = #{sche_end},"
			+ "is_important = #{is_important},alarm = #{alarm}"
			+ "WHERE sche_no = #{sche_no}")
	public void updateSchedule(ScheduleVO vo);
	
}

