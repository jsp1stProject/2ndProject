package com.sist.web.mypage.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class SitterAppDTO {
    private int user_no,app_no;
    private String pet_no,history,info,license,user_mail,nickname,user_name,status,addr,dbday,dbbirth;
    private Date regdate,birthday;
}
