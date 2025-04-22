package com.sist.web.chat.group.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupVO {
	private int group_no, capacity, owner;
	private String group_name, profile_img, description;
	private LocalDateTime create_at;
}
