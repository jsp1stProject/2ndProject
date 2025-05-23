package com.sist.web.mypage.service;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.PetErrorCode;
import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.PetException;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.mypage.mapper.MypageMapper;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.PetVO;
import com.sist.web.mypage.vo.SitterAppDTO;
import com.sist.web.mypage.vo.SitterInfoDTO;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import com.sist.web.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final UserMapper userMapper;
    private final MypageMapper mypageMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;
    private final SecurityUtil securityUtil;

    public String getRolls(String token) {
        if(token==null){
            throw new CommonException(CommonErrorCode.SC_UNAUTHORIZED);
        }
        List<String> list=jwtTokenProvider.getRoles(token);
        if(list==null){
            throw new CommonException(CommonErrorCode.NOT_FOUND);
        }
        return list.get(0);
    }

    @Override
    public UserDetailDTO getMyinfo(String token) {
        String userno=securityUtil.getValidUserNo(token);
        return userMapper.getUserDtoFromUserNo(userno);
    }

    @Override
    public void updateMyinfo(String token, UserDetailDTO dto, MultipartFile file, int isChange) {
        String userno=securityUtil.getValidUserNo(token);
        String usermail=dto.getUser_mail();
        String orig_mail=userMapper.getUserMailFromUserNo(userno).getUser_mail();
        int count=userMapper.getUserMailCount(usermail);

        //이메일 변경 && 이미 가입된 이메일
        if(!orig_mail.equals(usermail) && count!=0){
            throw new UserException(UserErrorCode.CONFLICT_EMAIL);
        }

        String db_pwd=userMapper.getPassword(orig_mail);
        String changed_pwd=dto.getOrig_pwd();
        String social_id=userMapper.getSocialId(orig_mail);
        Boolean isSocial=true;

        //일반 회원 && 비밀번호 확인 불일치
        if(social_id==null || social_id.isEmpty() || social_id.equals("")){
            isSocial=false;

            if(!passwordEncoder.matches(changed_pwd,db_pwd)){
                throw new UserException(UserErrorCode.NOT_MATCH_PWD);
            }
        }

        //소셜 회원 && 이메일 변경
        if(!orig_mail.equals(usermail) && isSocial){
            usermail=orig_mail; //이메일 변경 거부
        }

        //수정 실행
        UserVO vo=new UserVO();
        vo.setUser_no(Long.parseLong(userno));
        vo.setUser_mail(usermail);
        vo.setUser_name(dto.getUser_name());
        vo.setNickname(dto.getNickname());
        vo.setBirthday(dto.getBirthday());
        vo.setPhone(dto.getPhone());
        vo.setAddr(dto.getAddr());
        log.debug("service: {}",dto.getAddr());

        //비밀번호 변경 요청
        if(dto.getNew_pwd()!=null && dto.getNew_pwd().length()>=6){
            vo.setPassword(passwordEncoder.encode(dto.getNew_pwd()));
            log.debug("password changed to "+dto.getNew_pwd());
        }else if(isSocial){
            //새 비밀번호를 입력하지 않은 소셜 회원은 null값 그대로
        }else{
            vo.setPassword(passwordEncoder.encode(dto.getOrig_pwd()));
        }

        //프로필사진 변경
        if(isChange==1){
            //기본이미지로
            if(file==null || file.isEmpty()){
                awsS3Service.deleteFile(dto.getProfile());
                vo.setProfile("");
            }else{
                String storedFileName=insertOrUpdateProfileImage(token, file);
                vo.setProfile(storedFileName);
            }
        }else{
            vo.setProfile(dto.getProfile());
        }
        log.debug("newpassword:"+dto.getNew_pwd());
        log.debug("origpassword:"+dto.getOrig_pwd());
        log.debug("setted pwd:"+vo.getPassword());
        userMapper.updateUser(vo);
    }

    @Override
    public String insertOrUpdateProfileImage(String token, MultipartFile file) {
        UserDetailDTO dto=getMyinfo(token);
        if(dto.getProfile()!=null){ //이미 프사가 있으면
            //기존 프사 삭제
            awsS3Service.deleteFile(dto.getProfile());
        }
        //새로운 프사 업로드
        String storedFileName = "";
        String fileDir = "profile/";

        try {
            storedFileName=awsS3Service.ResizeAndUploadFile(file, fileDir, 120, 120);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }
        return storedFileName;
    }

    @Override
    public String insertOrUpdatePetProfileImage(String token, MultipartFile file, String petno) {
        String userno=securityUtil.getValidUserNo(token);
        PetDTO dto=mypageMapper.getMyPetDetail(userno,petno);

        if(dto!=null){
            if(dto.getPet_profilepic()!=null){
                //이미 프사가 있으면 기존 프사 삭제
                awsS3Service.deleteFile(dto.getPet_profilepic());
            }
        }
        //새로운 프사 업로드
        String storedFileName = "";
        String fileDir = "petprofile/";
        try {
            storedFileName=awsS3Service.ResizeAndUploadFile(file, fileDir, 120, 120);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }
        return storedFileName;
    }

    @Override
    public List<PetDTO> getMyPets(String token) {
        String userno=securityUtil.getValidUserNo(token);
        return mypageMapper.getMyPets(userno);
    }

    @Override
    public PetDTO getMyPetDetail(String token, String petno) {
        String userno=securityUtil.getValidUserNo(token);
        if(mypageMapper.checkPetExists(userno,petno) == 0){
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }
        return mypageMapper.getMyPetDetail(userno,petno);
    }

    @Override
    public void updateMyPetDetail(String token, PetDTO dto, MultipartFile file, int isChange) {
        String userno=securityUtil.getValidUserNo(token);
        String petno=dto.getPet_no();
        int count=mypageMapper.checkPetExists(userno,petno);

        if(count==0){
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }

        //수정 실행
        PetVO vo=new PetVO();
        vo.setPet_no(dto.getPet_no());
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
        if(isChange==1){
            //기본이미지로
            if(file==null || file.isEmpty()){
                awsS3Service.deleteFile(dto.getPet_profilepic());
                vo.setPet_profilepic("");
            }else{
                String storedFileName=insertOrUpdatePetProfileImage(token, file, petno);
                vo.setPet_profilepic(storedFileName);
            }
        }else{
            log.debug("isChange == 0");
            vo.setPet_profilepic(dto.getPet_profilepic());
        }
        mypageMapper.updateMyPet(vo);
    }

    @Override
    public void applyOrUpdatePetsitter(String token, SitterAppDTO dto) {
        String roll=getRolls(token);
        if(!roll.equals("ROLE_USER")){
            throw new PetException(PetErrorCode.CONFLICT_APPLY);
        }
        
        if(dto.getLicense()!=null){
            dto.setLicense("반려동물종합관리사");
        }
        String userno=securityUtil.getValidUserNo(token);
        SitterAppDTO origApp= mypageMapper.getAppSitter(userno);
        dto.setUser_no(Integer.parseInt(userno));
        
        //이미 신청서가 존재하면 수정, 없으면 새로 등록
        if(origApp!=null){
            mypageMapper.updateAppSitter(dto);
        }else{
            dto.setUser_no(Integer.parseInt(userno));
            mypageMapper.applyPetsitter(dto);
        }
        
    }

    @Override
    public SitterAppDTO getPetsitter(String token) {
        String userno=securityUtil.getValidUserNo(token);
        SitterAppDTO origApp= mypageMapper.getAppSitter(userno);
        if(origApp==null){
            throw new PetException(PetErrorCode.NO_APPLY);
        }
        return origApp;
    }

    @Override
    public SitterInfoDTO getPetsitterInfo(String token) {
        String userno=securityUtil.getValidUserNo(token);
        SitterInfoDTO info= mypageMapper.getSitterInfo(userno);
        if(info==null){
            throw new PetException(PetErrorCode.NO_APPLY);
        }
        return info;
    }


}
