package com.sist.web.feed.vo;

import java.util.*;
/*
FEEDNO              NUMBER         
GROUPNO    NOT NULL NUMBER         
USER_ID     NOT NULL VARCHAR2(200)  
TITLE       NOT NULL VARCHAR2(4000) 
CONTENT     NOT NULL CLOB           
FILECOUNT            NUMBER         
REGDATE              DATE           
UPDATE_TIME          DATE           

 */

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FeedVO {
	private int feed_no, group_no,filecount,comment_count, like_count, is_liked;
	private long user_no;
	private String title,content,dbday,nickname,profile;
	private Date regdate, update_time;
	private List<MultipartFile> files= new ArrayList<MultipartFile>();
	private List<String> images = new ArrayList<>();
}
