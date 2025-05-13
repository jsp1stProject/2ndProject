package com.sist.web.sitter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sist.web.sitter.vo.*;
import com.sist.web.sitter.service.*;
@RestController
public class SitterRestController {
	@Autowired
	private SitterService service;
		// 목록
	    @GetMapping("/sitter/list_vue")
	    public ResponseEntity<Map> sitter_list(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "care_loc") String fd,
	        @RequestParam(required = false) String ss
	    ) {
	        Map<String, Object> queryMap = new HashMap<>();
	        Map<String, Object> result = new HashMap<>();

	        int rowSize = 8;
	        int start = (page - 1) * rowSize + 1;
	        int end = page * rowSize;

	        queryMap.put("start", start);
	        queryMap.put("end", end);

	        List<SitterVO> list;
	        if (ss == null || ss.trim().isEmpty()) {
	            list = service.sitterListDataAll(queryMap);
	        } else {
	            queryMap.put("fd", fd);
	            queryMap.put("st", ss);
	            list = service.sitterListDataWithFilter(queryMap);
	        }

	        int totalpage = service.sitterTotalPage();
	        int BLOCK = 10;
	        int startPage = ((page - 1) / BLOCK * BLOCK) + 1;
	        int endPage = startPage + BLOCK - 1;
	        if (endPage > totalpage) endPage = totalpage;

	        result.put("list", list);
	        result.put("curpage", page);
	        result.put("totalpage", totalpage);
	        result.put("startPage", startPage);
	        result.put("endPage", endPage);

	        return new ResponseEntity<>(result, HttpStatus.OK);
	    }

	    // 상세보기
	    @GetMapping("/sitter/detail_vue")
	    public ResponseEntity<SitterVO> sitter_detail(int sitter_no) {
	        try {
	        	System.out.println("detail_vue============================");
	            System.out.println("받은 sitter_no: " + sitter_no);

	            SitterVO vo = service.sitterDetailData(sitter_no);
	            if (vo == null) {
	                System.out.println("sitterDetailData 리턴값이 null입니다.");
	                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	            }

	            return new ResponseEntity<>(vo, HttpStatus.OK);
	        } catch (Exception ex) {
	            System.out.println("🔥 서버 오류 발생:");
	            ex.printStackTrace(); // 로그 확인 필수
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 새글
	    @PostMapping("/sitter/insert")
	    public ResponseEntity<String> sitter_insert(@RequestBody SitterVO vo, HttpSession session) {
	        try {
	            int user_no = (int) session.getAttribute("user_no");

	            // 펫시터 자격 확인
	            if (!service.isSitter(user_no)) {
	                return new ResponseEntity<>("sitter XX", HttpStatus.FORBIDDEN);
	            }

	            // 게시글 중복 확인
	            if (service.hasSitterPost(user_no)) {
	                return new ResponseEntity<>("이미 등록되어있습니다", HttpStatus.CONFLICT);
	            }

	            // 등록 처리
	            vo.setUser_no(user_no);
	            service.sitterInsert(vo);
	            return new ResponseEntity<>("입력 완료", HttpStatus.OK);

	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return new ResponseEntity<>("입력 실패", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 수정
	    @PostMapping("/sitter/update")
	    public ResponseEntity<String> sitter_update(@RequestBody SitterVO vo) {
	        try {
	            service.sitterUpdate(vo);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 삭제
	    @DeleteMapping("/sitter/delete")
	    public ResponseEntity<String> sitter_delete(@RequestParam int sitter_no) {
	        try {
	            service.sitterDelete(sitter_no);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    // 리뷰
	    // 목록
	    @GetMapping("/sitter/review")
	    public ResponseEntity<List<SitterReviewVO>> review_list(@RequestParam int sitter_no) {
	        try {
	            List<SitterReviewVO> list = service.reviewListData(sitter_no);
	            return new ResponseEntity<>(list, HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 삽입
	    @PostMapping("/sitter/review")
	    public ResponseEntity<String> review_insert(@RequestBody SitterReviewVO vo) {
	        try {
	            service.reviewInsert(vo);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 대댓글 
	    @PostMapping("/sitter/review/reply")
	    public ResponseEntity<String> reply_insert(@RequestBody SitterReviewVO vo, HttpSession session) {
	        try {
	            service.replyInsert(vo);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 수정
	    @PutMapping("/sitter/review")
	    public ResponseEntity<String> review_update(@RequestBody SitterReviewVO vo) {
	        try {
	            service.reviewUpdate(vo);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // 삭제
	    @DeleteMapping("/sitter/review")
	    public ResponseEntity<String> review_delete(@RequestParam int review_no) {
	        try {
	            service.reviewDelete(review_no);
	            return new ResponseEntity<>("success", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }



}
