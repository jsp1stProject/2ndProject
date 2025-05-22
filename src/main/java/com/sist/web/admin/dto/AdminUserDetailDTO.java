package com.sist.web.admin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AdminUserDetailDTO {
    private String user_mail,social_id,user_name,nickname,phone,profile,addr,orig_pwd,new_pwd,db_birthday,db_regdate;
    private int enabled;
    private Long user_no;
    private Date regdate,birthday;
    //p_autority 테이블 join
    private String authority;
    //p_sitter
    private int sitter_no,app_no,jjimcount,carecount,active;
    private String tag,care_loc,sitter_pic,content,pet_first_price,info,license,active_price,history;
    private float score;
}
