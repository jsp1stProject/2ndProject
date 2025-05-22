package com.sist.web.group.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter @Setter
public class GroupMemberInfoDTO {
	private Long groupNo;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp joinedAt;
    private String role;
    private String profile;
}
