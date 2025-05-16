package com.sist.web.board.mapper;

import java.util.*;
import org.apache.ibatis.annotations.*;

import com.sist.web.board.vo.BoardVO;

@Mapper
public interface BoardMapper {
	@Select("SELECT post_id, user_no, nickname, type, title, content, created_at, views, like_count, image_url "
		      + "FROM (SELECT post_id, user_no, nickname, type, title, content, created_at, views, like_count, image_url, rownum AS num "
		      + "FROM (SELECT b.post_id, b.user_no, u.nickname, b.type, b.title, b.content, b.created_at, b.views, b.like_count, b.image_url "
		      + "FROM p_board_post b "
		      + "LEFT JOIN p_users u ON b.user_no = u.user_no "
		      + "ORDER BY post_id DESC)) "
		      + "WHERE num BETWEEN #{start} AND #{end}")
	public List<BoardVO> boardListData(Map map);



    @Select("SELECT * FROM p_board_post WHERE post_id = #{post_id}")
    public BoardVO boardDetail(@Param("post_id") int post_id);

    @Insert("INSERT INTO p_board_post(post_id, user_no, type, title, content, image_url, created_at, views, like_count) "
            +"VALUES (p_board_post_seq.nextval, #{user_no}, #{type}, #{title}, #{content}, #{image_url}, SYSDATE, 0, 0)")
    public void boardInsert(BoardVO vo);

    @Update("UPDATE p_board_post SET title = #{title}, content = #{content}, type = #{type}, image_url = #{image_url} WHERE post_id = #{post_id}")
    public void boardUpdate(BoardVO vo);

    @Delete("DELETE FROM p_board_post WHERE post_id = #{post_id}")
    public void boardDelete(@Param("post_id") int post_id);

    @Update("UPDATE p_board_post SET views = views + 1 WHERE post_id = #{post_id}")
    public void boardHit(@Param("post_id") int post_id);

    // 게시글 개수 (페이징용)
    @Select("SELECT COUNT(*) FROM p_board_post")
    public int boardTotalCount();
}
