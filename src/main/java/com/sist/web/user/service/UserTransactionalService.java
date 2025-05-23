package com.sist.web.user.service;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserTransactionalService {
    private final UserMapper userMapper;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void insertDefaultUser(UserVO vo) {
        //일반 회원가입
        int count = userMapper.getUserMailCount(vo.getUser_mail());
        //가입되지 않은 이메일이면
        if(count == 0) {
            Long userno=userMapper.getNextUserNo();
            vo.setUser_no(userno);
            userMapper.insertDefaultUser(vo);
            Map<String, Object> map = new HashMap<>();
            map.put("user_no",userno);
            map.put("authority", "ROLE_USER");
            userMapper.insertUserAuthority(map);
        } else {
            //동일한 메일이 이미 가입되어 있으면
            throw new UserException(UserErrorCode.CONFLICT_EMAIL);
        }
    }

    @Transactional
    public void insertKakaoUser(UserVO vo) {
        //카카오 회원 가입
        Long userno=userMapper.getNextUserNo();
        vo.setUser_no(userno);

        //가입 전 프사 업로드
        try {
            String storedFileName=awsS3Service.resizeAndUploadFromUrl(vo.getProfile(), "profile/", 120, 120);
            vo.setProfile(storedFileName);
        }catch (Exception e){
            log.error(e.getMessage());
            vo.setProfile("");
        }

        userMapper.insertKakaoUser(vo);
        Map<String, Object> map = new HashMap<>();
        map.put("user_no",userno);
        map.put("authority", "ROLE_USER");
        userMapper.insertUserAuthority(map);
    }
}
