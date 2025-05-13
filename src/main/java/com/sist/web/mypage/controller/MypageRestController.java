package com.sist.web.mypage.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageRestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    @PostMapping("")
    public ResponseEntity GetMyinfo(Model model, @CookieValue(value="accessToken") String token) {
        if(token!=null){
            String userno=jwtTokenProvider.getUserNoFromToken(token);
            UserDetailDTO dto= userMapper.getUserDtoFromUserNo(userno);
            return ResponseEntity.ok(ApiResponse.success(dto));
        }
        return ResponseEntity.badRequest().build();
    }
}
