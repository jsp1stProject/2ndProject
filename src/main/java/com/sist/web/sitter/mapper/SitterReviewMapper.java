package com.sist.web.sitter.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sist.web.sitter.vo.*;

public interface SitterReviewMapper {

    // 리뷰 작성
    @Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id) "
           + "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, #{rev_score}, #{rev_comment}, "
           + "(SELECT NVL(MAX(group_id)+1, 1) FROM p_sitter_review))")
    public void insertReview(SitterReviewVO vo);

    // 대댓글 작성
    @Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id, group_step, parent_no) "
            + "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, NULL, #{rev_comment}, #{group_id}, 1, #{parent_no})")
    public void insertReply(SitterReviewVO vo);

    // 특정 리뷰 단건 조회 (대댓글 권한 확인용)
    @Select("SELECT * FROM p_sitter_review WHERE review_no = #{review_no}")
    public SitterReviewVO selectReviewById(int review_no);
    
    // 게시물 삭제에 따른 리뷰 전체 삭제
    @Delete("DELETE FROM p_sitter_review WHERE sitter_no = #{sitter_no}")
    public void deleteReviewsBySitterNo(@Param("sitter_no") int sitter_no);
    
    @Select("SELECT r.*, u.nickname, u.user_no AS u_user_no, u.profile " +
            "FROM p_sitter_review r " +
            "JOIN p_users u ON r.user_no = u.user_no " +
            "WHERE r.sitter_no = #{sitter_no} " +
            "ORDER BY r.group_id DESC, r.group_step ASC")
    @Results({
        @Result(property = "review_no", column = "review_no"),
        @Result(property = "rev_comment", column = "rev_comment"),
        @Result(property = "rev_score", column = "rev_score"),
        @Result(property = "group_id", column = "group_id"),
        @Result(property = "group_step", column = "group_step"),
        @Result(property = "parent_no", column = "parent_no"),
        // 유저 정보 매핑
        @Result(property = "user.nickname", column = "nickname"),
        @Result(property = "user.user_no", column = "u_user_no"),
        @Result(property = "user.profile", column = "profile")
    })
    public List<SitterReviewVO> reviewList(@Param("sitter_no") int sitter_no);

    
    // 리뷰 삭제
    @Delete("DELETE FROM p_sitter_review WHERE review_no = #{review_no}")
    public int deleteReview(@Param("review_no") int review_no);
    
    // 리뷰 수정
    @Update("UPDATE p_sitter_review SET rev_comment = #{rev_comment}, rev_score = #{rev_score} WHERE review_no = #{review_no}")
    public int updateReview(SitterReviewVO vo);

    
    

    
}
