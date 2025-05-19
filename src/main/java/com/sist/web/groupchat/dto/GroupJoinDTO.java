package com.sist.web.groupchat.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class GroupJoinDTO {
	@NotNull(message = "그룹 번호는 필수입니다")
	@Positive(message = "그룹 번호는 1 이상의 값이어야 합니다")
    private Long groupNo;
    private String nickname;
}