package com.sist.web.groupchat.dto;

import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter @Setter
public class MessageSearchFilterDTO {
	private Integer groupNo;
	private String keyword;
	private String senderNickname;
	private String startDate;
	private String endDate;
	
	public boolean isValidDate(String date) {
		return date == null || date.isBlank() || date.matches("\\d{4}-\\d{2}-\\d{2}");
	}

	public boolean isValid() {
		return isValidDate(startDate) && isValidDate(endDate);
	}
}
