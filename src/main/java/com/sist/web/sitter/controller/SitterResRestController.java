package com.sist.web.sitter.controller;

import com.sist.web.sitter.service.*;
import com.sist.web.sitter.vo.*;
import com.sist.web.aws.AwsS3Service;
import com.sist.web.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;
@RestController
public class SitterResRestController {

	@Autowired
	private SitterResService rService;

	@Autowired
	private SitterResPetService pService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private AwsS3Service AwsS3Service;

	// 예약 목록
	@GetMapping("/sitter/resList_vue")
	public ResponseEntity<Map<String, Object>> res_list(@RequestParam int page,
			@CookieValue(name = "accessToken", required = false) String token) {
		Map<String, Object> map = new HashMap<>();
		int user_no = parseUserNo(token);
		if (user_no == -1) {
			map.put("status", "unauthorized");
			return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
		}

		try {
			List<SitterResVO> list = rService.sitterReservationList(user_no);
			map.put("list", list);
			map.put("status", "success");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			map.put("status", "fail");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 예약 상세
	@GetMapping("/sitter/resDetail_vue")
	public ResponseEntity<?> reserve_detail(@RequestParam int res_no,
			@CookieValue(name = "accessToken", required = false) String token) {
		int user_no = parseUserNo(token);
		if (user_no == -1)
			return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);

		try {
			SitterResVO vo = rService.sitterReservation(res_no);
			if (vo.getUser_no() != user_no)
				return new ResponseEntity<>("forbidden", HttpStatus.FORBIDDEN);

			List<PetsVO> pets = pService.getPetsByResNo(res_no);
			vo.setPetsList(pets);
			return new ResponseEntity<>(vo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 예약 등록
	@PostMapping("/sitter/reserve_vue")
	public ResponseEntity<String> reserve_vue(@RequestBody SitterResVO vo,
			@CookieValue(name = "accessToken", required = false) String token) {
		int user_no = parseUserNo(token);
		if (user_no == -1)
			return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);

		try {
			vo.setUser_no(user_no);
			rService.insertSitterRes(vo);
			for (Integer pet_no : vo.getPet_nos()) {
	            pService.insertResPet(vo.getRes_no(), pet_no);
	        }
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/sitter/res/disabled_hours")
	public ResponseEntity<Map<String, Object>> getDisabledHours(
	        @RequestParam int sitter_no,
	        @RequestParam String res_date) {

		Date sqlDate = Date.valueOf(res_date);
	    List<Map<String, String>> reserved = rService.getReservedTimeRanges(sitter_no, sqlDate);
	    Set<Integer> disabledHours = new HashSet<>();

	    for (Map<String, String> r : reserved) {
	        int start = Integer.parseInt(r.get("start_time").substring(0, 2));
	        int end = Integer.parseInt(r.get("end_time").substring(0, 2));
	        for (int h = start; h < end; h++) {
	            disabledHours.add(h);
	        }
	    }

	    Map<String, Object> result = new HashMap<>();
	    result.put("disabledHours", disabledHours);
	    return ResponseEntity.ok(result);
	}

	// 결제 완료 후 호출
	@PostMapping("/sitter/res/pay_complete")
	public ResponseEntity<String> payComplete(@RequestParam("res_no") int res_no,
			@RequestParam("imp_uid") String imp_uid, @CookieValue(name = "accessToken", required = false) String token) {
		int user_no = parseUserNo(token);
		if (user_no == -1)
			return new ResponseEntity<>("unauthorized", HttpStatus.UNAUTHORIZED);

		try {
			SitterResVO vo = rService.sitterReservation(res_no);
			if (vo.getUser_no() != user_no)
				return new ResponseEntity<>("forbidden", HttpStatus.FORBIDDEN);

			rService.updatePayStatus(res_no, "결제완료", imp_uid);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 이중 예약 확인
	@GetMapping("/sitter/res/check")
	public ResponseEntity<Map<String, Boolean>> checkConflict(@RequestParam int sitter_no,
			@RequestParam String res_date, @RequestParam String start_time, @RequestParam String end_time) {
		try {
			boolean conflict = rService.isConflict(sitter_no, res_date, start_time, end_time);
			Map<String, Boolean> result = new HashMap<>();
			result.put("conflict", conflict);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 내부 토큰 파싱
	private int parseUserNo(String token) {
		if (token == null || token.isEmpty())
			return -1;
		try {
			return Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
		} catch (Exception e) {
			return -1;
		}
	}
}
