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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRestController {
    private final UserService userService;
    private final UserTransactionalService userTransactionalService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("refreshToken", null);
        cookie2.setMaxAge(0);
        cookie2.setHttpOnly(true);
        cookie2.setPath("/");
        response.addCookie(cookie2);

        return ResponseEntity.ok("logout successful");
    }

    @PostMapping("/kakao/join")
    public ResponseEntity kakaoJoin(@RequestBody Map<String, String> data, Model model) {
        String kakaoAccessToken = userService.GetKakaoAccessToken(data.get("code"));
        return userService.GetInsertKakaoUser(kakaoAccessToken);
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
