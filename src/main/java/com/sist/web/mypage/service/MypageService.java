package com.sist.web.mypage.service;

import com.sist.web.user.vo.UserDetailDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MypageService {
    public String getValidUserNo(String token);
    public UserDetailDTO getMyinfo(String token);
    public void updateMyinfo(String token, UserDetailDTO dto, MultipartFile file, int isChange);
    public String insertOrUpdateProfileImage(String token, MultipartFile file);
}
