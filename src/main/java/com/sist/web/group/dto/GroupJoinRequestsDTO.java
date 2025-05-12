package com.sist.web.group.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GroupJoinRequestsDTO {
	private int request_no, group_no;
	private long user_no;
	private String status, dbday;
	private Date request_date;
}
