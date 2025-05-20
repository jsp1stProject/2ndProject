package com.sist.web.mypage.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class SitterDTO {
    private int user_no,app_no;
    private String pet_no,history,info,license,user_mail,nickname,user_name,status,dbday;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regdate;
}
