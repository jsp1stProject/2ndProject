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
	/*
	 * public List<BoardVO> boardListData(Map map);

    // �Խñ� �� ��ȸ
    @Select("SELECT * FROM p_board_post WHERE post_id = #{post_id}")
    public BoardVO boardDetail(@Param("post_id") int post_id);

    // �Խñ� �ۼ�
    @Insert("INSERT INTO p_board_post(post_id, user_no, type, title, content, created_at, views, like_count) "
        +"VALUES (seq_board_post.nextval, #{user_no}, #{type}, #{title}, #{content}, SYSDATE, 0, 0)")
    public void boardInsert(BoardVO vo);

    // �Խñ� ����
    @Update("UPDATE p_board_post SET title = #{title}, content = #{content}, type = #{type} WHERE post_id = #{post_id}")
    public void boardUpdate(BoardVO vo);

    // �Խñ� ����
    @Delete("DELETE FROM p_board_post WHERE post_id = #{post_id}")
    public void boardDelete(@Param("post_id") int post_id);

    // ��ȸ�� ����
    @Update("UPDATE p_board_post SET views = views + 1 WHERE post_id = #{post_id}")
    public void boardHit(@Param("post_id") int post_id);

    // �Խñ� ���� (����¡��)
    @Select("SELECT COUNT(*) FROM p_board_post")
    public int boardTotalCount();
	 */
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
