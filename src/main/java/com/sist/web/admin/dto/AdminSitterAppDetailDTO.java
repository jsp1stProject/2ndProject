package com.sist.web.admin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class AdminSitterAppDetailDTO {
    private int user_no,app_no;
    private String pet_no,history,info,license,user_mail,nickname,user_name,status,addr,dbday,dbbirth,profile;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date regdate,birthday;
}