package com.sist.web.user.controller;

import com.sist.web.user.service.UserService;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
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

        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/kakao/join")
    public ResponseEntity<String> kakaoJoin(@RequestBody Map<String, String> data, Model model) {
        String kakaoAccessToken = userService.GetKakaoAccessToken(data.get("code"));
        return userService.GetInsertKakaoUser(kakaoAccessToken);
    }
}
