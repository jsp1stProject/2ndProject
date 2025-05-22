package com.sist.web.mypage.service;

import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.user.vo.UserDetailDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MypageService {
    public UserDetailDTO getMyinfo(String token);
    public void updateMyinfo(String token, UserDetailDTO dto, MultipartFile file, int isChange);
    public String insertOrUpdateProfileImage(String token, MultipartFile file);
    public String insertOrUpdatePetProfileImage(String token, MultipartFile file, String petno);
    public List<PetDTO> getMyPets(String token);
    public PetDTO getMyPetDetail(String token, String petno);
    public void updateMyPetDetail(String token, PetDTO dto, MultipartFile file, int isChange);
    public void applyOrUpdatePetsitter(String token, SitterDTO dto);
    public SitterDTO getPetsitter(String token);
}
