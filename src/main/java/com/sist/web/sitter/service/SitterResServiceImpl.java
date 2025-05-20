package com.sist.web.sitter.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public void updatePayStatus(int res_no, String status, String imp_uid) {
		// TODO Auto-generated method stub
		rDao.updatePayStatus(res_no, status, imp_uid);
	}

	@Override
	public int checkReservationConflict(int sitter_no, Date res_date, String start_time, String end_time) {
		// TODO Auto-generated method stub
		return rDao.checkReservationConflict(sitter_no, res_date, start_time, end_time);
	}

	@Override
	public void cancelReservationBySitter(int res_no) {
		// TODO Auto-generated method stub
		rDao.cancelReservationBySitter(res_no);
	}

	@Override
	public List<Map<String, String>> getReservedTimeRanges(int sitter_no, Date res_date) {
		// TODO Auto-generated method stub
		return rDao.getReservedTimeRanges(sitter_no, res_date);
	}

	@Override
	public boolean isConflict(int sitter_no, String res_date, String start_time, String end_time) {
	    try {
	        Date sqlDate = java.sql.Date.valueOf(res_date); // 문자열 → java.sql.Date로 변환
	        int count = checkReservationConflict(sitter_no, sqlDate, start_time, end_time);
	        return count > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return true; // 예외 발생 시 중복된 걸로 간주 (안전하게)
	    }
	}

}
