package com.sist.web.admin.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
public class AdminSitterListDTO {
    private int user_no,app_no;
    private String pet_no,history,info,license,user_mail,nickname,user_name,status,addr,dbday,dbbirth;
    private Date regdate,birthday;
}