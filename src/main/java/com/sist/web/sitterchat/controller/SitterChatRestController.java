package com.sist.web.sitterchat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.sitterchat.service.SitterChatService;
import com.sist.web.sitterchat.vo.*;

@RestController
@RequestMapping("/sitterchat")
public class SitterChatRestController {

    @Autowired
    private SitterChatService service;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 내부 토큰 파싱
    private int parseUserNo(String token) {
        if (token == null || token.isEmpty()) return -1;
        try {
            return Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
        } catch (Exception e) {
            return -1;
        }
    }

    // ✅ 1. 채팅방 목록 조회 (좌측 사이드바)
    @GetMapping("/list_vue")
    public ResponseEntity<Map<String, Object>> chat_list(
        @CookieValue(name = "accessToken", required = false) String token,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rowSize
    ) {
        Map<String, Object> result = new HashMap<>();
        int userNo = parseUserNo(token);
        if (userNo == -1) {
            result.put("status", "NO_LOGIN");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        int start = (page - 1) * rowSize + 1;
        int end = page * rowSize;

        List<SitterChatRoomVO> list;
        int totalpage;
        
        list = service.selectChatRoomList(userNo);
        totalpage = service.selectChatRoomTotalPage();
        int BLOCK = 10;
        int startPage = ((page - 1) / BLOCK) * BLOCK + 1;
        int endPage = Math.min(startPage + BLOCK - 1, totalpage);

        result.put("list", list);
        result.put("curpage", page);
        result.put("totalpage", totalpage);
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        result.put("status", "OK");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // ✅ 2. 메시지 목록 조회 (채팅방 진입 시)
    @GetMapping("/msglist")
    public ResponseEntity<?> msg_list(@RequestParam int room_no,
        @CookieValue(name = "accessToken", required = false) String token) {

        int userNo = parseUserNo(token);
        if (userNo == -1) return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);

        SitterChatRoomVO room = service.SitterChatRoomById(room_no);
        if (room == null || (room.getUser1_no() != userNo && room.getUser2_no() != userNo)) {
            return new ResponseEntity<>("forbidden", HttpStatus.FORBIDDEN);
        }

        List<SitterChatVO> list = service.selectChatList(room_no);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // ✅ 3. 메시지 저장 (fallback 또는 WebSocket 사용 시 함께 저장)
    @PostMapping("/send")
    public ResponseEntity<String> msg_insert(@RequestBody SitterChatVO vo) {
        service.insertChat(vo);
        return new ResponseEntity<>("SENT", HttpStatus.OK);
    }

    // ✅ 4. 메시지 키워드 검색
    @GetMapping("/search")
    public ResponseEntity<List<SitterChatVO>> msg_search(
        @RequestParam int room_id,
        @RequestParam String keyword
    ) {
        List<SitterChatVO> list = service.searchChatByKeyword(room_id, keyword);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
