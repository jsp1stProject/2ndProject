package com.sist.web.sitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.sitter.dao.SitterResPetDAO;
import com.sist.web.sitter.vo.PetsVO;

@Service
public class SitterResPetServiceImpl implements SitterResPetService{
	@Autowired
	private SitterResPetDAO pDao;
	@Override
	public void insertResPet(int res_no, int pet_no) {
		// TODO Auto-generated method stub
		pDao.insertResPet(res_no, pet_no);
	}

	@Override
	public List<PetsVO> getPetsByResNo(int res_no) {
		// TODO Auto-generated method stub
		return pDao.getPetsByResNo(res_no);
	}

	@Override
	public List<PetsVO> getPetsByUserNo(int user_no) {
		// TODO Auto-generated method stub
		return pDao.getPetsByUserNo(user_no);
	}

	@Override
	public void deleteReservePetBySitterNo(int sitter_no) {
		// TODO Auto-generated method stub
		pDao.deleteReservePetBySitterNo(sitter_no);
	}

	

}
