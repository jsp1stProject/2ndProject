package com.sist.web.sitter.vo;
import java.util.*;

import com.sist.web.user.vo.UserVO;

import lombok.Data;

@Data
public class SitterResVO {
    // 예약 기본 정보
    private int res_no;
    private int user_no;
    private int sitter_no;
    private Date res_date;
    private String start_time;
    private String end_time;
    private String location_type;      // '신청자 집', '거리', '기타'
    private String location_detail;
    private int total_price;
    private String pay_status;         // '미결제', '결제완료'
    private String res_status;         // '대기중', '승인', '거절'
    private Date created_at;
    private List<Integer> pet_nos; // 예약 시 선택된 반려동물 번호 목록
    
    private UserVO userVO;
    private SitterVO sitterVO;
    private List<PetsVO> petsList;
}