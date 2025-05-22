package com.sist.web.user.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;
    private final UserTransactionalService userTransactionalService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/kakao/join")
    public ResponseEntity<Void> kakaoJoin(@RequestBody Map<String, String> data, HttpServletResponse res) {
        String baseUrl = data.get("url");

        String kakaoAccessToken = userService.getKakaoAccessToken(data.get("code"),baseUrl);
        userService.insertOrLoginKakaoUser(kakaoAccessToken, res);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/join")
    public ResponseEntity<String> join_ok(@ModelAttribute UserVO vo, Model model) {
        log.info(vo.toString());
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        userTransactionalService.insertDefaultUser(vo);

        model.addAttribute("user_name", vo.getUser_name());
        return ResponseEntity.ok("가입 완료");

    }

    @PostMapping("/users/{userno}")
    public ResponseEntity<ApiResponse<UserDetailDTO>> getUserDetail(@PathVariable("userno") String userno, Model model) {
        UserDetailDTO dto=userService.getActiveUserDetail(userno);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/header")
    public ResponseEntity<ApiResponse<UserDetailDTO>> getHeaderDetail(
            @CookieValue(value="accessToken", required = false) String token) {
        UserDetailDTO dto = userService.getHeaderDetail(token);
        return ResponseEntity.ok(ApiResponse.success(dto,"https://pet4u.s3.ap-northeast-2.amazonaws.com/"));
    }
}
