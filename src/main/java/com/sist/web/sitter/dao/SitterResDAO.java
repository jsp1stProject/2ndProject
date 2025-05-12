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
	public List<PetsVO> getPetsByUserNo(int user_no)
	{
		return mapper.getPetsByUserNo(user_no);
	}
}
