package com.sist.web.mypage.service;

import com.sist.web.common.exception.code.PetErrorCode;
import com.sist.web.common.exception.domain.PetException;
import com.sist.web.mypage.mapper.MypageMapper;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.PetVO;
import com.sist.web.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageTransactionalService {
    private final MypageMapper mypageMapper;
    private final MypageService mypageService;
    private final SecurityUtil securityUtil;

    public void insertMyPetDetail(String token, PetDTO dto, MultipartFile file, int isChange) {
        String userno=securityUtil.getValidUserNo(token);
        String petno=String.valueOf(mypageMapper.getNextPetNo());

        PetVO vo=new PetVO();
        vo.setPet_no(petno);
        vo.setUser_no(userno);
        vo.setPet_name(dto.getPet_name());
        vo.setPet_age(dto.getPet_age());
        vo.setPet_weight(Float.parseFloat(dto.getPet_weight()));
        vo.setPet_type(dto.getPet_type());
        vo.setPet_subtype(dto.getPet_subtype());
        vo.setPet_char1(dto.getPet_char1()==null?0:1);
        vo.setPet_char2(dto.getPet_char2()==null?0:1);
        vo.setPet_char3(dto.getPet_char3()==null?0:1);
        vo.setPet_status(dto.getPet_status()==null?0:1);
        //프로필사진 변경
        if(isChange==1 && (file!=null || !file.isEmpty())) {
            String storedFileName=mypageService.insertOrUpdatePetProfileImage(token, file, petno);
            vo.setPet_profilepic(storedFileName);
        }else{
            throw new PetException(PetErrorCode.NO_PROFILE);
        }
        mypageMapper.insertMyPet(vo);
    }
}
