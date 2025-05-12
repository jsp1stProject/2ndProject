package com.sist.web.sitter.vo;

import com.sist.web.user.vo.UserVO;

/*
 * PET_NO         NOT NULL NUMBER         
USER_NO                 NUMBER         
PET_NAME       NOT NULL VARCHAR2(51)   
PET_PROFILEPIC NOT NULL VARCHAR2(2000) 
PET_AGE        NOT NULL NUMBER         
PET_TYPE       NOT NULL VARCHAR2(50)   
PET_SUBTYPE             VARCHAR2(50)   
PET_CHAR1               NUMBER         
PET_CHAR2               NUMBER         
PET_CHAR3               NUMBER         
PET_STATUS              NUMBER    
 */
public class PetsVO {
	private int pet_no,user_no,pet_age,pet_char1,pet_char2,pet_char3,pet_status;
	private String pet_name,pet_profilepic,pet_type,pet_subtype;
	
	private UserVO user;
}
