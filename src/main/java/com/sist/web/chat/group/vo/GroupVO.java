package com.sist.web.chat.group.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupVO {
	private long group_id;
	private int capacity;
	private String group_name, profile_img, description, created_by;
	private LocalDateTime create_at;
}
