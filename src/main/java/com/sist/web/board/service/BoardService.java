package com.sist.web.board.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.*;
import com.sist.web.board.vo.*;
public interface BoardService {
	public List<BoardVO> boardListData(Map map);
	public BoardVO boardDetail(@Param("post_id") int post_id);
	public void boardInsert(BoardVO vo);
	public void boardUpdate(BoardVO vo);
	public void boardDelete(@Param("post_id") int post_id);
	public void boardHit(@Param("post_id") int post_id);
	public int boardTotalCount();
}
