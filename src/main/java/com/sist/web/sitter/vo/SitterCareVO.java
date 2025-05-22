package com.sist.web.sitter.vo;
import com.sist.web.user.vo.*;
import com.sist.web.sitter.vo.*;
import java.util.Date;

import lombok.Data;
/*
CARE_NO         NOT NULL NUMBER        
PET_NO                   NUMBER        
USER_NO                  NUMBER        
SITTER_NO                NUMBER        
CARE_NUM        NOT NULL NUMBER        
CARE_TIME       NOT NULL VARCHAR2(30)  
CARE_MEET       NOT NULL VARCHAR2(100) 
CARE_DATE                DATE          
CARE_STATUS              NUMBER        
PET_TOTAL_PRICE NOT NULL VARCHAR2(30)  
PET_PRICE       NOT NULL VARCHAR2(30)
 */
@Data
public class SitterCareVO {
	private int care_no,pet_no,user_no,sitter_no,care_num,care_status;
	private Date care_date;
	private String care_time,care_meet,pet_total_price,pet_price;
	
	private SitterVO sitter; // p_sitter
	private UserVO user; // p_users
	private PetsVO pets;// p_pets
}
