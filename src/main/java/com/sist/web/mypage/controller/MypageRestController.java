package com.sist.web.mypage.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.mypage.service.MypageService;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.service.UserService;
import com.sist.web.user.service.UserTransactionalService;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/mypage")
public class MypageRestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final MypageService mypageService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<UserDetailDTO>> GetMyinfo(@CookieValue(value="accessToken", required = false) String token) {
        UserDetailDTO dto = mypageService.getMyinfo(token);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> UpdateMyinfo(
            @ModelAttribute UserDetailDTO dto,
            @RequestParam(value="profile", required = false)MultipartFile profile,
            @CookieValue(value="accessToken", required = false) String token) {
        log.debug(dto.toString());
        mypageService.updateMyinfo(token,dto);
        return ResponseEntity.ok().build();
    }

}
