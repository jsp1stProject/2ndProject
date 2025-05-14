package com.sist.web.mypage.service;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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
        UserDetailDTO dto=userMapper.getUserDtoFromUserNo(userno);

        return userMapper.getUserDtoFromUserNo(userno);
    }

    @Override
    public void updateMyinfo(String token, UserDetailDTO dto) {
        String userno=getValidUserNo(token);
        String usermail=dto.getUser_mail();
        String orig_mail=userMapper.getUserMailFromUserNo(userno).getUser_mail();
        int count=userMapper.getUserMailCount(usermail);
        //이메일을 변경했는데 이미 가입된 이메일이면
        if(!orig_mail.equals(usermail) && count!=0){
            throw new UserException(UserErrorCode.CONFLICT_EMAIL);
        }
        String db_pwd=userMapper.getPassword(orig_mail);
        String req_pwd=dto.getOrig_pwd();
        String social_id=userMapper.getSocialId(orig_mail);
        Boolean isSocial=true;
        //소셜 가입 회원이 아니면 기존 비밀번호 확인
        if(social_id==null || social_id.isEmpty() || social_id.equals("")){
            //기존 비번 불일치
            isSocial=false;
            if(!passwordEncoder.matches(req_pwd,db_pwd)){
                throw new UserException(UserErrorCode.NOT_MATCH_PWD);
            }
        }
        //이메일을 변경했는데 소셜 회원이면 변경 불가능처리
        if(!orig_mail.equals(usermail) && isSocial){
            usermail=orig_mail;
        }

        //수정처리
        UserVO vo=new UserVO();
        vo.setUser_no(Long.parseLong(userno));
        vo.setUser_mail(usermail);
        vo.setUser_name(dto.getUser_name());
        vo.setNickname(dto.getNickname());
        vo.setProfile(dto.getProfile());
        vo.setBirthday(dto.getBirthday());
        vo.setPhone(dto.getPhone());
        vo.setAddr(dto.getAddr());
        if(dto.getNew_pwd()!=null && dto.getNew_pwd().length()>=6){
            //새 비밀번호를 입력함
            vo.setPassword(passwordEncoder.encode(dto.getNew_pwd()));
            log.debug("password changed to "+dto.getNew_pwd());
        }else if(isSocial){
            //새 비밀번호를 입력하지 않은 소셜 회원은 null값 그대로
        }else{
            vo.setPassword(passwordEncoder.encode(dto.getOrig_pwd()));
        }
        log.debug("newpassword:"+dto.getNew_pwd());
        log.debug("origpassword:"+dto.getOrig_pwd());
        log.debug("setted pwd:"+vo.getPassword());
        userMapper.updateUser(vo);
    }
}
