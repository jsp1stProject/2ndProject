package com.sist.web.sitter.mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sist.web.sitter.vo.*;

public interface SitterReviewMapper {

    // 댓글 (리뷰) 작성
    @Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id) " +
            "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, #{rev_score}, #{rev_comment}, " +
            "(SELECT NVL(MAX(group_id)+1, 1) FROM p_sitter_review))")
    void insertReview(SitterReviewVO vo);

    // 대댓글 작성
    @Insert("INSERT INTO p_sitter_review(review_no, sitter_no, user_no, rev_score, rev_comment, group_id, group_step, parent_no) " +
            "VALUES(p_sitrev_no_seq.NEXTVAL, #{sitter_no}, #{user_no}, NULL, #{rev_comment}, #{group_id}, 1, #{parent_no})")
    void insertReply(SitterReviewVO vo);

    // 특정 리뷰 단건 조회 (대댓글 권한 확인용)
    @Select("SELECT * FROM p_sitter_review WHERE review_no = #{review_no}")
    SitterReviewVO selectReviewById(int review_no);
}
