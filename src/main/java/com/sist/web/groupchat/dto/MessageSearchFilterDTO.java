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
	@Size(max = 20, message = "닉네임은 20자 이하여야 합니다.")
	private String senderNickname;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
	private String startDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
	private String endDate;
}
