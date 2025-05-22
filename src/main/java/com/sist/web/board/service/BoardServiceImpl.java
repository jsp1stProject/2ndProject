package com.sist.web.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.board.dao.BoardDAO;
import com.sist.web.board.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDAO bDao;

	@Override
	public List<BoardVO> boardListData(Map map) {
		// TODO Auto-generated method stub
		return bDao.boardListData(map);
	}

	@Override
	public BoardVO boardDetail(int post_id) {
		// TODO Auto-generated method stub
		return bDao.boardDetail(post_id);
	}

	@Override
	public void boardInsert(BoardVO vo) {
		// TODO Auto-generated method stub
		bDao.boardInsert(vo);
	}

	@Override
	public void boardUpdate(BoardVO vo) {
		// TODO Auto-generated method stub
		bDao.boardUpdate(vo);
	}

	@Override
	public void boardDelete(int post_id) {
		// TODO Auto-generated method stub
		bDao.boardDelete(post_id);
	}

	@Override
	public void boardHit(int post_id) {
		// TODO Auto-generated method stub
		bDao.boardHit(post_id);
	}

	@Override
	public int boardTotalCount() {
		// TODO Auto-generated method stub
		return bDao.boardTotalCount();
	}
	
}
