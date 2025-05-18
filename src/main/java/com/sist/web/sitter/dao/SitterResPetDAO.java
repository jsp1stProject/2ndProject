package com.sist.web.sitter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sist.web.sitter.mapper.SitterResPetMapper;
import com.sist.web.sitter.vo.PetsVO;
@Repository
public class SitterResPetDAO {
	@Autowired SitterResPetMapper mapper;
	
	public void insertResPet(int res_no,int pet_no)
	{
		mapper.insertResPet(res_no, pet_no);
	}
	public List<PetsVO> getPetsByResNo(int res_no)
	{
		return mapper.getPetsByResNo(res_no);
	}
	public List<PetsVO> getPetsByUserNo(int user_no)
	{
		return mapper.getPetsByResNo(user_no);
	}
}
