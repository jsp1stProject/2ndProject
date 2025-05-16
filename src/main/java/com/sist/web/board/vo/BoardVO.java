package com.sist.web.board.vo;
/*
 * ---------- -------- ------------- 
POST_ID    NOT NULL NUMBER        
USER_NO    NOT NULL NUMBER        
TYPE       NOT NULL VARCHAR2(20)  
TITLE      NOT NULL VARCHAR2(200) 
CONTENT    NOT NULL CLOB          
CREATED_AT          DATE          
VIEWS               NUMBER        
LIKE_COUNT          NUMBER   
 */
import java.util.*;

import lombok.Data;
@Data
public class BoardVO {
	private int post_id,user_no,views,like_count,reply_count;
	private String type,title,content,image_url,nickname;
	private Date created_at;
}
