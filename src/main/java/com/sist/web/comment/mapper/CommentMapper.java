package com.sist.web.comment.mapper;

import java.util.*;
import org.apache.ibatis.annotations.*;
import com.sist.web.comment.vo.CommentVO;

@Mapper
public interface CommentMapper {
    @Select("SELECT c.comment_id, c.post_id, c.user_no, c.content, c.created_at, u.nickname "
        +"FROM p_board_comment c "
        +"JOIN p_users u ON c.user_no = u.user_no "
        +"WHERE c.post_id = #{post_id} "
        +"ORDER BY c.comment_id DESC")
    public List<CommentVO> commentList(int post_id);

    @Insert("INSERT INTO p_board_comment (comment_id, post_id, user_no, content) "
        +"VALUES (seq_board_comment.nextval, #{post_id}, #{user_no}, #{content})")
    public void commentInsert(CommentVO vo);

    @Delete("DELETE FROM p_board_comment WHERE comment_id = #{comment_id}")
    public void commentDelete(int comment_id);
}
