package com.sist.web.sitter.dao;
import java.util.*;

import com.sist.web.sitter.mapper.SitterResMapper;
import com.sist.web.sitter.vo.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SitterResDAO {
	@Autowired
	private SitterResMapper mapper;
	
	public SitterResVO sitterReservation(int res_no)
	{
		return mapper.sitterReservation(res_no);
	}
	public List<SitterResVO> sitterReservationList(int user_no)
	{
		return mapper.sitterReservationList(user_no);
	}
	public void insertSitterRes(SitterResVO vo)
	{
		mapper.insertSitterRes(vo);
	}
	public void updatePayStatus(int res_no, String status, String imp_uid)
	{
		mapper.updatePayStatus(res_no, status, imp_uid);
	}
	public int checkReservationConflict(int sitter_no, Date res_date,String start_time, String end_time)
	{
		return mapper.checkReservationConflict(sitter_no, res_date, start_time, end_time);
	}
	public void cancelReservationBySitter(int res_no)
	{
		mapper.cancelReservationBySitter(res_no);
	}
	public List<Map<String, String>> getReservedTimeRanges(int sitter_no, Date res_date)
	{
		return mapper.getReservedTimeRanges(sitter_no, res_date);
	}
}
