package com.sist.web.group.dto;

import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
	private Integer group_no;
	private Integer capacity;
	@NotNull(message = "생성자는 필수값입니다")
	private Integer owner;
	@NotNull(message = "그룹 이름은 필수값입니다")
	private String group_name;
	private String owner_name;
	private String is_public;
	//is_member, join_status => 그룹가입여부 확인을 위한 컬럼
	private String is_member;
	private String join_status; 
	private Integer current_member_count;
	private String description;
	@ToString.Exclude
	private String profile_img;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp created_at;
}
