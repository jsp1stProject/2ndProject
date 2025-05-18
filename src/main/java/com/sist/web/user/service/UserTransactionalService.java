package com.sist.web.user.service;

import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Transactional
public class UserTransactionalService {
    private final UserMapper userMapper;

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
        userMapper.insertKakaoUser(vo);
        Map<String, Object> map = new HashMap<>();
        map.put("user_no",userno);
        map.put("authority", "ROLE_USER");
        userMapper.insertUserAuthority(map);
    }
}
