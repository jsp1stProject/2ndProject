package com.sist.web.feed.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleVO {
	private int sche_no, group_no, type, is_public;
	private long user_no;
	private String sche_title, sche_content, dbday;
	private Date sche_start, sche_end, regdate;
	
	//알람추가예정
}
