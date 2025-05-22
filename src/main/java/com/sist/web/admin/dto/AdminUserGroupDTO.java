package com.sist.web.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserGroupDTO {
    private int group_no,user_no;
    private String group_name,profile_img,nickname,joined_at,role;
}
