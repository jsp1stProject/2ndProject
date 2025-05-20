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
    
    @Autowired
	private AwsS3Service AwsS3Service;

    // 내부 토큰 파싱
    private int parseUserNo(String token) {
        if (token == null || token.isEmpty()) return -1;
        try {
            return Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
        } catch (Exception e) {
            return -1;
        }
    }

    // 채팅방 목록 조회 (검색 + 페이지네이션)
    @GetMapping("/list_vue")
    public ResponseEntity<Map<String, Object>> chat_list(
        @CookieValue(name = "accesstoken", required = false) String token,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int rowSize,
        @RequestParam(defaultValue = "nickname") String fd,
        @RequestParam(required = false) String st
    ) {
        Map<String, Object> result = new HashMap<>();
        int userNo = parseUserNo(token);

        if (userNo == -1) {
            result.put("status", "NO_LOGIN");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        int start = (page - 1) * rowSize + 1;
        int end = page * rowSize;

        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);
        map.put("start", start);
        map.put("end", end);

        List<SitterChatRoomVO> list;
        int totalpage;

        if (st != null && !st.trim().isEmpty()) {
            map.put("fd", fd);
            map.put("st", st);
            list = service.SitterChatRoomListWithFilter(map);
            totalpage = service.SitterChatRoomTotalPageWithFilter(map);
        } else {
            list = service.SitterChatRoomList(userNo, start, end);
            totalpage = service.SitterChatRoomTotalPage(userNo, rowSize);
        }

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

    // 채팅방 생성
    @PostMapping("/insert")
    public ResponseEntity<String> chat_insert(@RequestBody SitterChatRoomVO vo) {
        service.SitterChatRoomInsert(vo);
        return new ResponseEntity<>("INSERTED", HttpStatus.OK);
    }

    // 채팅방 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> chat_delete(@RequestParam int room_id) {
        service.SitterChatRoomDelete(room_id);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    // 메시지 저장
    @PostMapping("/send")
    public ResponseEntity<String> msg_insert(@RequestBody SitterChatVO vo) {
        service.SitterChatInsert(vo);
        return new ResponseEntity<>("SENT", HttpStatus.OK);
    }

    // 메시지 목록 조회
    @GetMapping("/msglist")
    public ResponseEntity<List<SitterChatVO>> msg_list(@RequestParam int room_id) {
        List<SitterChatVO> list = service.SitterChatList(room_id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 메시지 키워드 검색
    @GetMapping("/search")
    public ResponseEntity<List<SitterChatVO>> msg_search(
        @RequestParam int room_id,
        @RequestParam String keyword
    ) {
        List<SitterChatVO> list = service.SitterChatSearch(room_id, keyword);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
