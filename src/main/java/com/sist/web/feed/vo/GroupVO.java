package com.sist.web.feed.vo;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupVO {
	private int group_no, capacity, owner;
	private String group_name, profile_img, description;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp created_at;
}
