package com.sist.web.sitter.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sist.web.sitter.vo.PetsVO;
import com.sist.web.sitter.vo.SitterResVO;

public interface SitterResService {
	public SitterResVO sitterReservation(int res_no);
	public List<SitterResVO> sitterReservationList(int user_no);
	public void insertSitterRes(SitterResVO vo);
	public List<PetsVO> getPetsByUserNo(int user_no);
}
