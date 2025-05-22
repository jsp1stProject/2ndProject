package com.sist.web.sitter.vo;
import com.sist.web.user.vo.*;
import lombok.Data;
/*
APP_NO  NOT NULL NUMBER         
PET_NO           NUMBER         
USER_NO          NUMBER         
HISTORY NOT NULL VARCHAR2(1000) 
LICENSE          VARCHAR2(50)   
INFO             VARCHAR2(2000) 
 */
@Data
public class SitterAppVO {
	private int app_no,pet_no,user_no;
	private String history,license,info;
	
	private UserVO user; // p_users
	private PetsVO pets;// p_pets
}
