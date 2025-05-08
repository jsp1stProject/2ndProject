package com.sist.web.groupchat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupJoinDTO {
    private String token;
    private Long groupNo;
    private String nickname;
}