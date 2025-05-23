package com.sist.web.sitter.vo;

import com.sist.web.user.vo.UserVO;

import lombok.Data;

@Data
public class SitterReviewVO {
    private int review_no;
    private int sitter_no;
    private int user_no;
    private Double rev_score;       // 대댓글은 null
    private String rev_comment;
    private int group_id;
    private Integer group_step;     // 0=댓글, 1=대댓글
    private Integer parent_no;      // 대댓글일 경우 대상 댓글
    
    private UserVO user;
}

