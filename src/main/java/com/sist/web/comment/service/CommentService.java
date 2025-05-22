package com.sist.web.comment.service;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import com.sist.web.comment.vo.CommentVO;

public interface CommentService {
	/*
	 * public List<CommentVO> commentList(int post_id);

    @Insert("INSERT INTO p_board_comment (comment_id, post_id, user_no, content) "
        +"VALUES (seq_board_comment.nextval, #{post_id}, #{user_no}, #{content})")
    public void commentInsert(CommentVO vo);

    @Delete("DELETE FROM p_board_comment WHERE comment_id = #{comment_id}")
    public void commentDelete(int comment_id);
	 */
	public List<CommentVO> commentList(int post_id);
	public void commentInsert(CommentVO vo);
	public void commentDelete(int comment_id);
}
