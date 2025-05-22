package com.sist.web.sitter.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sist.web.sitter.vo.PetsVO;
import com.sist.web.sitter.vo.SitterResVO;

public interface SitterResService {
	public SitterResVO sitterReservation(int res_no);
	public List<SitterResVO> sitterReservationList(int user_no);
	public void insertSitterRes(SitterResVO vo);
	public void updatePayStatus(int res_no, String status, String imp_uid);
	public int checkReservationConflict(int sitter_no, Date res_date,String start_time, String end_time);
	public void cancelReservationBySitter(int res_no);
	public List<Map<String, String>> getReservedTimeRanges(int sitter_no, Date res_date);
	public boolean isConflict(int sitter_no, String res_date, String start_time, String end_time);
}
	