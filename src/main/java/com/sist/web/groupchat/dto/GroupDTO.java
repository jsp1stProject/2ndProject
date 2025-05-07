package com.sist.web.groupchat.dto;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupDTO {
	private Integer group_no;
	private Integer capacity;
	@NotNull(message = "생성자는 필수값입니다")
	private Integer owner;
	@NotNull(message = "그룹 이름은 필수값입니다")
	private String group_name;
	private String description;
	private String profile_img;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp create_at;
}
