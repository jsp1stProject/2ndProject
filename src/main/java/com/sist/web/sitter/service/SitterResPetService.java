package com.sist.web.sitter.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sist.web.sitter.vo.PetsVO;

public interface SitterResPetService {
	public void insertResPet(int res_no,int pet_no);
	public List<PetsVO> getPetsByResNo(int res_no);
	public List<PetsVO> getPetsByUserNo(int user_no);
	public void deleteReservePetBySitterNo(int sitter_no);
}
