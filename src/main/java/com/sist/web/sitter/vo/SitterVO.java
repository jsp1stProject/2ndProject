package com.sist.web.sitter.vo;
import com.sist.web.user.vo.*;
import lombok.Data;
/*
SITTER_NO       NOT NULL NUMBER         
APP_NO                   NUMBER         
USER_NO                  NUMBER         
JJIMCOUNT                NUMBER         
SCORE                    NUMBER(2,1)    
CARECOUNT       NOT NULL NUMBER         
TAG                      VARCHAR2(200)  
CONTENT         NOT NULL VARCHAR2(2000) 
SITTER_PIC               VARCHAR2(100)  
CARE_LOC        NOT NULL VARCHAR2(1000) 
PET_FIRST_PRICE          VARCHAR2(30) 
 */
@Data
public class SitterVO {
	private int sitter_no,app_no,user_no,jjimcount,carecount;
	private Double score;
	private String tag,content,sitter_pic,care_loc,pet_first_price;
	private boolean jjimCheck; // 현재 로그인한 유저가 이 펫시터를 찜했는지 여부
	
	private UserVO user = new UserVO(); // p_users
	private SitterAppVO sitterApp = new SitterAppVO(); // p_sitter_app
}
