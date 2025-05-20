package com.sist.web.group.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupMemberDTO {
	@NotNull(message = "그룹 번호는 필수값입니다")
	private Integer group_no;
	@NotNull(message = "유저 번호는 필수값입니다")
	private Integer user_no;
	private Long last_read_message;
	private String nickname, role, status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp joined_at, left_at;
	private String profile;
}
