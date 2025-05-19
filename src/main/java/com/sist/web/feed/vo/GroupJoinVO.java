package com.sist.web.feed.vo;

import java.util.Date;

import lombok.Data;

@Data
public class GroupJoinVO {
	private int request_no, group_no, user_no;
	private String status, dbday;
	private Date request_date;
}
