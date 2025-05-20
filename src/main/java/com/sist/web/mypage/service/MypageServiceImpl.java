package com.sist.web.mypage.service;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Service awsS3Service;

    public String getValidUserNo(String token) {
        if(token==null){
            throw new CommonException(CommonErrorCode.SC_UNAUTHORIZED);
        }
        String userno=jwtTokenProvider.getUserNoFromToken(token);
        if(userno==null){
            throw new CommonException(CommonErrorCode.NOT_FOUND);
        }
        return userno;
    }

    @Override
    public UserDetailDTO getMyinfo(String token) {
        String userno=getValidUserNo(token);
        return userMapper.getUserDtoFromUserNo(userno);
    }

    @Override
    public void updateMyinfo(String token, UserDetailDTO dto, MultipartFile file, int isChange) {
        String userno=getValidUserNo(token);
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
            log.debug("stored image name1 "+storedFileName);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }
        log.debug("stored image name2 "+storedFileName);
        return storedFileName;
    }


}
