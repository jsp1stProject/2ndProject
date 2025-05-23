package com.sist.web.feed.vo;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class FeedCommentVO {
	private int no, group_no, feed_no, group_step, group_id ;
	private long user_no;
	private String msg, dbday, nickname,profile;
	private Date regdate; 
	
	private List<FeedCommentVO> replies; // 대댓글
}
