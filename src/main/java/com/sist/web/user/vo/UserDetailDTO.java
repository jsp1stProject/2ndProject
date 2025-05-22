package com.sist.web.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserDetailDTO {
    private String user_mail,social_id,user_name,nickname,phone,profile,addr,orig_pwd,new_pwd,db_birthday;
    private int enabled;
    private Long user_no;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regdate,birthday;
    private String authority;//p_autority 테이블 join
    private String care_loc;//p_sitter
    private float score;//p_sitter
}
