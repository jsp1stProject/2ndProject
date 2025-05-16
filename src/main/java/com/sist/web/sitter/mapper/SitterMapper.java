package com.sist.web.sitter.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sist.web.sitter.vo.*;

public interface SitterMapper {
	// ✅ 전체 목록
    @Select("SELECT sitter_no, jjimcount, carecount, score, tag, content, sitter_pic, care_loc, pet_first_price, "
          + "nickname, user_name, history, license, info, num " 
          + "FROM (SELECT sitter_no, jjimcount, carecount, score, tag, content, sitter_pic, care_loc, pet_first_price, "
          + "nickname, user_name, history, license, info, rownum AS num " 
          + "FROM (SELECT ps.sitter_no, ps.jjimcount, ps.carecount, ps.score, ps.tag, ps.content, ps.sitter_pic, "
          + "ps.care_loc, ps.pet_first_price, pu.nickname, pu.user_name, spa.history, spa.license, spa.info "
          + "FROM p_sitter ps "
          + "JOIN p_users pu ON ps.user_no = pu.user_no "
          + "JOIN p_sitter_app spa ON ps.app_no = spa.app_no "
          + "ORDER BY ps.sitter_no ASC)) "
          + "WHERE num BETWEEN #{start} AND #{end}")
    @Results({
        @Result(property = "sitter_no", column = "sitter_no"),
        @Result(property = "jjimcount", column = "jjimcount"),
        @Result(property = "carecount", column = "carecount"),
        @Result(property = "score", column = "score"),
        @Result(property = "tag", column = "tag"),
        @Result(property = "content", column = "content"),
        @Result(property = "sitter_pic", column = "sitter_pic"),
        @Result(property = "care_loc", column = "care_loc"),
        @Result(property = "pet_first_price", column = "pet_first_price"),

        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.user_name", column = "user_name"),

        @Result(property = "sitterApp.history", column = "history"),
        @Result(property = "sitterApp.license", column = "license"),
        @Result(property = "sitterApp.info", column = "info")
    })
    public List<SitterVO> sitterListDataAll(Map map);

    // ✅ 필터 포함 목록
    @Select("SELECT sitter_no, jjimcount, carecount, score, tag, content, sitter_pic, care_loc, pet_first_price, "
          + "nickname, user_name, history, license, info, num " 
          + "FROM (SELECT sitter_no, jjimcount, carecount, score, tag, content, sitter_pic, care_loc, pet_first_price, "
          + "nickname, user_name, history, license, info, rownum AS num " 
          + "FROM (SELECT ps.sitter_no, ps.jjimcount, ps.carecount, ps.score, ps.tag, ps.content, ps.sitter_pic, "
          + "ps.care_loc, ps.pet_first_price, pu.nickname, pu.user_name, spa.history, spa.license, spa.info "
          + "FROM p_sitter ps "
          + "JOIN p_users pu ON ps.user_no = pu.user_no "
          + "JOIN p_sitter_app spa ON ps.app_no = spa.app_no "
          + "WHERE ${fd} IN (SELECT REGEXP_SUBSTR(#{st}, '[^,]+', 1, LEVEL) "
          + "FROM dual CONNECT BY LEVEL <= LENGTH(REGEXP_REPLACE(#{st}, '[^,]', '')) + 1) "
          + "ORDER BY ps.sitter_no ASC)) "
          + "WHERE num BETWEEN #{start} AND #{end}")
    @Results({
        @Result(property = "sitter_no", column = "sitter_no"),
        @Result(property = "jjimcount", column = "jjimcount"),
        @Result(property = "carecount", column = "carecount"),
        @Result(property = "score", column = "score"),
        @Result(property = "tag", column = "tag"),
        @Result(property = "content", column = "content"),
        @Result(property = "sitter_pic", column = "sitter_pic"),
        @Result(property = "care_loc", column = "care_loc"),
        @Result(property = "pet_first_price", column = "pet_first_price"),

        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.user_name", column = "user_name"),

        @Result(property = "sitterApp.history", column = "history"),
        @Result(property = "sitterApp.license", column = "license"),
        @Result(property = "sitterApp.info", column = "info")
    })
    public List<SitterVO> sitterListDataWithFilter(Map map);

    // ✅ 총 페이지 수
    @Select("SELECT CEIL(COUNT(*) / 8.0) FROM p_sitter")
    public int sitterTotalPage();

    // ✅ 상세보기
    @Select("SELECT s.*, "
          + "u.user_no AS u_user_no, u.nickname AS u_nickname, u.user_name AS u_user_name, "
          + "a.history AS a_history, a.license AS a_license, a.info AS a_info "
          + "FROM p_sitter s "
          + "JOIN p_users u ON s.user_no = u.user_no "
          + "JOIN p_sitter_app a ON s.app_no = a.app_no "
          + "WHERE s.sitter_no = #{sitter_no}")
    @Results({
        @Result(property = "sitter_no", column = "sitter_no"),
        @Result(property = "app_no", column = "app_no"),
        @Result(property = "user_no", column = "user_no"),
        @Result(property = "jjimcount", column = "jjimcount"),
        @Result(property = "carecount", column = "carecount"),
        @Result(property = "score", column = "score"),
        @Result(property = "tag", column = "tag"),
        @Result(property = "content", column = "content"),
        @Result(property = "sitter_pic", column = "sitter_pic"),
        @Result(property = "care_loc", column = "care_loc"),
        @Result(property = "pet_first_price", column = "pet_first_price"),

        @Result(property = "user.user_no", column = "u_user_no"),
        @Result(property = "user.nickname", column = "u_nickname"),
        @Result(property = "user.user_name", column = "u_user_name"),

        @Result(property = "sitterApp.history", column = "a_history"),
        @Result(property = "sitterApp.license", column = "a_license"),
        @Result(property = "sitterApp.info", column = "a_info")
    })
    public SitterVO sitterDetailData(int sitter_no);
    
	// 펫시터 여부 (p_sitter_app)
	@Select("SELECT COUNT(*) FROM p_sitter_app WHERE user_no = #{user_no}")
	public int isSitter(int user_no);

	// 펫시터 게시판 중복 여부
	@Select("SELECT COUNT(*) FROM p_sitter WHERE user_no = #{user_no}")
	public int hasSitterPost(int user_no);

	// 새글
	@Insert("INSERT INTO p_sitter(sitter_no,user_no,carecount,tag,content,sitter_pic,care_loc,pet_first_price) "
			+ "VALUES(p_sit_no_seq.NEXTVAL,#{user_no},#{carecount},#{tag},#{content},#{sitter_pic},#{care_loc},#{pet_first_price})")
	public void sitterInsert(SitterVO vo);
	
	// 수정
	@Update("UPDATE p_sitter SET carecount=#{carecount},tag=#{tag},content=#{content},sitter_pic=#{sitter_pic},care_loc=#{care_loc},pet_first_price=#{pet_first_price} "
			+ "WHERE sitter_no=#{sitter_no}")
	public void sitterUpdate(SitterVO vo);
	
	// 삭제
	@Delete("DELETE FROM p_sitter WHERE sitter_no=#{sitter_no}")
	public void sitterDelete(int sitter_no);
	
	// 리뷰 목록
	@Select("SELECT r.*, u.nickname, u.user_name " +
	        "FROM p_sitter_review r " +
	        "JOIN p_users u ON r.user_no = u.user_no " +
	        "WHERE r.sitter_no = #{sitter_no} " +
	        "ORDER BY group_id DESC, group_step ASC")
	@Results({
	    @Result(property = "review_no", column = "review_no"),
	    @Result(property = "care_no", column = "care_no"),
	    @Result(property = "sitter_no", column = "sitter_no"),
	    @Result(property = "user_no", column = "user_no"),
	    @Result(property = "rev_score", column = "rev_score"),
	    @Result(property = "rev_date", column = "rev_date"),
	    @Result(property = "rev_comment", column = "rev_comment"),
	    @Result(property = "group_id", column = "group_id"),
	    @Result(property = "group_step", column = "group_step"),

	    @Result(property = "user.nickname", column = "nickname"),
	    @Result(property = "user.user_name", column = "user_name")
	})
	public List<SitterReviewVO> reviewListData(int sitter_no);

	// 리뷰 작성
	@Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id) " +
	        "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, #{rev_score}, #{rev_comment}, " +
	        "(SELECT NVL(MAX(group_id)+1, 1) FROM p_sitter_review))")
	public void reviewInsert(SitterReviewVO vo);

	// 대댓글
	@Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id, group_step) " +
	        "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, NULL, #{rev_comment}, #{group_id}, #{group_step})")
	public void replyInsert(SitterReviewVO vo);

	// 리뷰 수정
	@Update("UPDATE p_sitter_review SET rev_comment = #{rev_comment}, rev_score = #{rev_score} " +
	        "WHERE review_no = #{review_no}")
	public void reviewUpdate(SitterReviewVO vo);

	// 단일 리뷰 삭제
	@Delete("DELETE FROM p_sitter_review WHERE review_no = #{review_no}")
	public void reviewDelete(int review_no);
	
	// 게시물 삭제에 따른 리뷰 전체 삭제
	@Delete("DELETE FROM p_sitter_review WHERE sitter_no = #{sitter_no}")
	public void deleteSitterReviewWithPost(int sitter_no);
	
	// 찜하기
	@Select("SELECT ps.sitter_no, ps.app_no, ps.user_no, ps.jjimcount, ps.carecount, "
			+ "CASE WHEN EXISTS ( "
			+  "  SELECT 1 FROM p_jjim "
			+  "  WHERE user_no = #{user_no} AND sitter_no = ps.sitter_no "
			+  ") THEN 1 ELSE 0 END AS jjimCheck "
			+  "FROM p_sitter ps "
			+  "ORDER BY ps.sitter_no DESC")
	public List<SitterVO> jjimSitterList(@Param("user_no") int user_no);
	
	@Select("SELECT COUNT(*) FROM p_jjim WHERE user_no = #{user_no} AND sitter_no = #{sitter_no}")
	public int checkJjim(@Param("user_no") int user_no, @Param("sitter_no") int sitter_no);

	@Insert("INSERT INTO p_jjim(jjim_no, user_no, sitter_no) VALUES (jjim_seq.NEXTVAL, #{user_no}, #{sitter_no})")
	public void insertJjim(@Param("user_no") int user_no, @Param("sitter_no") int sitter_no);

	@Delete("DELETE FROM p_jjim WHERE user_no = #{user_no} AND sitter_no = #{sitter_no}")
	public void deleteJjim(@Param("user_no") int user_no, @Param("sitter_no") int sitter_no);

	@Update("UPDATE p_sitter SET jjimcount = jjimcount + #{amount} WHERE sitter_no = #{sitter_no}")
	public void updateJjimCount(@Param("sitter_no") int sitter_no, @Param("amount") int amount);


	
	
	
}
