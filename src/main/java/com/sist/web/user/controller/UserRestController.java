package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/auth")
public class UserRestController {
    private final UserService userService;
    private final UserTransactionalService userTransactionalService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/kakao/join")
    public ResponseEntity kakaoJoin(@RequestBody Map<String, String> data, HttpServletResponse res, HttpServletRequest request) {
        StringBuffer fullUrl = request.getRequestURL();
        String uri = request.getRequestURI();
        String baseUrl = fullUrl.substring(0, fullUrl.length() - uri.length());

        String kakaoAccessToken = userService.GetKakaoAccessToken(data.get("code"),baseUrl);
        return userService.InsertOrLoginKakaoUser(kakaoAccessToken, res);
    }

    @PostMapping("/join")
    public ResponseEntity join_ok(@ModelAttribute UserVO vo, Model model) {
        log.info(vo.toString());
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        userTransactionalService.insertDefaultUser(vo);

        model.addAttribute("user_name", vo.getUser_name());
        return ResponseEntity.ok("가입 완료");

    }
}
