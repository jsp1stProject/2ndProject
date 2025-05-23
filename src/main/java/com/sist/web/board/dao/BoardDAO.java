package com.sist.web.board.dao;
import java.util.*;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sist.web.board.vo.*;
import com.sist.web.board.mapper.*;
@Repository
public class BoardDAO {
	@Autowired
	private BoardMapper mapper;

	public List<BoardVO> boardListData(Map map){
		return mapper.boardListData(map);
	}
	
	public BoardVO boardDetail(@Param("post_id") int post_id) {
		return mapper.boardDetail(post_id);
	}
	public void boardInsert(BoardVO vo) {
		mapper.boardInsert(vo);
	}
	public void boardUpdate(BoardVO vo) {
		mapper.boardUpdate(vo);
	}
	public void boardDelete(@Param("post_id") int post_id) {
		mapper.boardDelete(post_id);
	}
	public void boardHit(@Param("post_id") int post_id) {
		mapper.boardHit(post_id);
	}
	public int boardTotalCount() {
		return mapper.boardTotalCount();
	}
}
