package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRestController {
    private final UserService userService;
    private final UserTransactionalService userTransactionalService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/kakao/join")
    public ResponseEntity kakaoJoin(@RequestBody Map<String, String> data, HttpServletResponse res, HttpServletRequest request) {
        StringBuffer fullUrl = request.getRequestURL();
        String uri      = request.getRequestURI();
        String baseUrl  = fullUrl.substring(0, fullUrl.length() - uri.length());

        String kakaoAccessToken = userService.GetKakaoAccessToken(data.get("code"),baseUrl);
        return userService.InsertOrLoginKakaoUser(kakaoAccessToken, res);
    }

    @PostMapping("/join")
    public ResponseEntity join_ok(@ModelAttribute UserVO vo, Model model) {
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        boolean isInsert=userTransactionalService.insertDefaultUser(vo);
        if(isInsert){
            model.addAttribute("user_name", vo.getUser_name());
            return ResponseEntity.ok("가입 완료");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 가입된 이메일입니다.");
        }

    }
}
