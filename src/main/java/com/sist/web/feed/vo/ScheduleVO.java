package com.sist.web.feed.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScheduleVO {
	private int sche_no, group_no, type, is_public, is_important, alarm;
	private long user_no;
	private String sche_title, sche_content,sche_start_str, sche_end_str,dbday;
	private Date sche_start, sche_end, regdate;
	private List<ScheduleMemberVO> participants; //스케쥴 참여 멤버조회
	private List<Long> participants_no;
	
}
