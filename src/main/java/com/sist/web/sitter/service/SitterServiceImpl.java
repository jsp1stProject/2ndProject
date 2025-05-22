package com.sist.web.sitter.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sist.web.sitter.dao.*;
import com.sist.web.sitter.vo.SitterReviewVO;
import com.sist.web.sitter.vo.SitterVO;

@Service
public class SitterServiceImpl implements SitterService{
	@Autowired
	private SitterDAO sDao;
	

	@Override
	public int sitterTotalPage() {
		// TODO Auto-generated method stub
		return sDao.sitterTotalPage();
	} 

	@Override
	public SitterVO sitterDetailData(int sitter_no) {
		// TODO Auto-generated method stub
		return sDao.sitterDetailData(sitter_no);
	}

	@Override
	public void sitterInsert(SitterVO vo) {
		// TODO Auto-generated method stub
		sDao.sitterInsert(vo);
	}

	@Override
	public void sitterUpdate(SitterVO vo) {
		// TODO Auto-generated method stub
		sDao.sitterUpdate(vo);
	}

	@Override
	public void sitterDelete(int sitter_no) {
		// TODO Auto-generated method stub
		sDao.sitterDelete(sitter_no);
	}

	@Override
	public List<SitterVO> sitterListDataAll(Map map) {
		// TODO Auto-generated method stub
		return sDao.sitterListDataAll(map);
	}

	@Override
	public List<SitterVO> sitterListDataWithFilter(Map map) {
		// TODO Auto-generated method stub
		return sDao.sitterListDataWithFilter(map);
	}

	@Override
	public boolean isSitter(int user_no) {
		// TODO Auto-generated method stub
		return sDao.isSitter(user_no) > 0;
	}

	@Override
	public boolean hasSitterPost(int user_no) {
		// TODO Auto-generated method stub
		return sDao.hasSitterPost(user_no) > 0;
	}

	@Override
	public List<SitterVO> jjimSitterList(int user_no) {
		// TODO Auto-generated method stub
		return sDao.jjimSitterList(user_no);
	}

	@Override
	public boolean toggleJjim(int user_no, int sitter_no) {
		// TODO Auto-generated method stub
		return sDao.toggleJjim(user_no, sitter_no);
	}

	@Override
	public void deleteJjimAll(int sitter_no) {
		// TODO Auto-generated method stub
		sDao.deleteJjimAll(sitter_no);
		
	}


}