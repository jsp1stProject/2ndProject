package com.sist.web.sitter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.sitter.dao.SitterResDAO;
import com.sist.web.sitter.vo.PetsVO;
import com.sist.web.sitter.vo.SitterResVO;

@Service
public class SitterResServiceImpl implements SitterResService{
	@Autowired
	private SitterResDAO rDao;

	@Override
	public SitterResVO sitterReservation(int res_no) {
		// TODO Auto-generated method stub
		return rDao.sitterReservation(res_no);
	}

	@Override
	public List<SitterResVO> sitterReservationList(int user_no) {
		// TODO Auto-generated method stub
		return rDao.sitterReservationList(user_no);
	}

	@Override
	public void insertSitterRes(SitterResVO vo) {
		// TODO Auto-generated method stub
		rDao.insertSitterRes(vo);
	}

	@Override
	public List<PetsVO> getPetsByUserNo(int user_no) {
		// TODO Auto-generated method stub
		return rDao.getPetsByUserNo(user_no);
	}

}
