package com.sist.web.sitter.vo;

import lombok.Data;
/*
JJIM_NO   NOT NULL NUMBER 
USER_NO   NOT NULL NUMBER 
SITTER_NO NOT NULL NUMBER 
 */
@Data
public class SitterJjimVO {
    private int jjim_no;    
    private int user_no;     
    private int sitter_no;   
    
    private SitterVO sitterVO;
}
