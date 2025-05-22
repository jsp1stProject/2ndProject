package com.sist.web.mypage.vo;

import lombok.Data;

@Data
public class PetDTO {
    private String pet_no,pet_name, pet_profilepic, pet_type, pet_subtype,pet_char1,pet_char2,pet_char3,pet_weight,pet_status;
    private int pet_age;
}
