package com.sist.web.sitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.sist.web.sitter.vo.*;
import com.sist.web.sitter.service.*;

@RestController
public class SitterResRestController {

    @Autowired
    private SitterResService rService;

    @Autowired
    private SitterResPetService pService;

    // 예약 목록
    @GetMapping("/sitter/resList_vue")
    public ResponseEntity<Map> res_list(@RequestParam int page, @RequestParam int user_no) {
        Map map = new HashMap<>();

        try {
            List<SitterResVO> list = rService.sitterReservationList(user_no);
            map.put("list", list);
            map.put("status", "success");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            map.put("status", "fail");
            return ResponseEntity.status(500).body(map);
        }
    }

    // 예약 상세
    @GetMapping("/sitter/resDetail_vue")
    public ResponseEntity<SitterResVO> reserve_detail(@RequestParam int res_no) {
        try {
            SitterResVO vo = rService.sitterReservation(res_no);
            List<PetsVO> pets = pService.getPetsByResNo(res_no);
            vo.setPetsList(pets);
            return ResponseEntity.ok(vo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 예약 등록
    @PostMapping("/sitter/reserve_vue")
    public ResponseEntity<String> reserve_vue(@RequestBody SitterResVO vo) {
        try {
            rService.insertSitterRes(vo);  // 내부에서 insertSitterRes + insertResPet 반복 처리
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("fail");
        }
    }
}
