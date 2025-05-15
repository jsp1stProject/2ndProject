package com.sist.web.sitter.vo;
import java.util.Date;

import com.sist.web.sitter.vo.*;
import com.sist.web.user.vo.*;
import lombok.Data;
/*
REVIEW_NO   NOT NULL NUMBER         
CARE_NO              NUMBER         
SITTER_NO            NUMBER         
USER_NO              NUMBER         
REV_SCORE            NUMBER(2,1)    
REV_DATE             DATE           
REV_COMMENT          VARCHAR2(2000) 
GROUP_ID             NUMBER         
GROUP_STEP           NUMBER   
 */
@Data
public class SitterReviewVO {
	private int review_no,care_no,sitter_no,user_no,group_id,group_step;
	private Double rev_score;
	private String rev_comment;
	private Date rev_date;
	private UserVO user; // p_users
	private SitterCareVO care; // p_sitter_care
}
