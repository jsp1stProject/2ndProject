package com.sist.web.sitter.controller;
import com.sist.web.security.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.sitter.vo.*;
import com.sist.web.sitter.service.*;
@RestController
public class SitterRestController {
	@Autowired
	private SitterService service;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	// 목록
	@GetMapping("/sitter/list_vue")
	public ResponseEntity<Map<String, Object>> sitter_list(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "care_loc") String fd,
	    @RequestParam(required = false) String st
	) {
	    Map<String, Object> queryMap = new HashMap<>();
	    Map<String, Object> result = new HashMap<>();

	    int rowSize = 8;
	    int start = (page - 1) * rowSize + 1;
	    int end = page * rowSize;

	    queryMap.put("start", start);
	    queryMap.put("end", end);

	    List<SitterVO> list;

	    // 필터
	    if (st == null || st.trim().isEmpty()) {
	        list = service.sitterListDataAll(queryMap);
	    } else {
	        queryMap.put("fd", fd);
	        queryMap.put("st", st);
	        list = service.sitterListDataWithFilter(queryMap);
	    }

	    int totalpage = service.sitterTotalPage();
	    int BLOCK = 10;
	    int startPage = ((page - 1) / BLOCK) * BLOCK + 1;
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
    public ResponseEntity<SitterVO> sitter_detail(@RequestParam("sitter_no") int sitter_no) {
        try {
            SitterVO vo = service.sitterDetailData(sitter_no);

            if (vo == null) {
                System.out.println("sitterDetailData 리턴값이 null입니다.");
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            // sitterApp 정보 확인용 로그
            if (vo.getSitterApp() != null) {
                System.out.println("license: " + vo.getSitterApp().getLicense());
            }

            return new ResponseEntity<>(vo, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println("🔥 서버 오류 발생:");
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 찜하기
    @PostMapping("/sitter/jjim/toggle")
    public ResponseEntity<String> toggleJjim(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody Map<String, Integer> data) {
        try {
            String token = authHeader.replace("Bearer ", "");
            int user_no = Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token)); 

            int sitter_no = data.get("sitter_no");
            boolean result = service.toggleJjim(user_no, sitter_no);
            return new ResponseEntity<>(result ? "찜 추가됨" : "찜 취소됨", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("인증 실패", HttpStatus.UNAUTHORIZED);
        }
    }


    //새글
    @PostMapping("/sitter/insert")
    public ResponseEntity<String> sitter_insert(
            @RequestParam("upload") MultipartFile file,
            @RequestParam("tag") String tag,
            @RequestParam("content") String content,
            @RequestParam("carecount") int carecount,
            @RequestParam("care_loc") String care_loc,
            @RequestParam("pet_first_price") String pet_first_price,
            HttpSession session) {
        try {
            // 파일 저장
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get("C:/upload/" + fileName);
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            // VO 구성
            SitterVO vo = new SitterVO();
            vo.setTag(tag);
            vo.setContent(content);
            vo.setCarecount(carecount);
            vo.setCare_loc(care_loc);
            vo.setPet_first_price(pet_first_price);
            vo.setSitter_pic("/upload/" + fileName);

            service.sitterInsert(vo);
            return new ResponseEntity<>("success", HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
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
	        	service.deleteSitterReviewWithPost(sitter_no);
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
