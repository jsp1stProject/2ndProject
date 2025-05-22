package com.sist.web.sitter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.sitter.service.*;
import com.sist.web.sitter.vo.SitterReviewVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sitter/review")
@RequiredArgsConstructor
public class SitterReviewRestController {

    private final SitterReviewService reviewService;
    private final JwtTokenProvider jwt;

 
    @PostMapping
    public ResponseEntity<ApiResponse<String>> insertReview(@RequestBody SitterReviewVO vo,
                                                            @CookieValue(name = "accessToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(ApiResponse.fail("401", "인증 실패"));
        }

        try {
            int user_no = Integer.parseInt(jwt.getUserNoFromToken(token));
            vo.setUser_no(user_no);
            reviewService.insertReview(vo);
            return ResponseEntity.ok(ApiResponse.success("리뷰 등록 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(ApiResponse.fail("500", "리뷰 등록 실패"));
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<ApiResponse<String>> insertReply(@RequestBody SitterReviewVO vo,
                                                           @CookieValue(name = "accessToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(ApiResponse.fail("401", "인증 실패"));
        }

        try {
            int user_no = Integer.parseInt(jwt.getUserNoFromToken(token));
            vo.setUser_no(user_no);
            reviewService.insertReply(vo);
            return ResponseEntity.ok(ApiResponse.success("대댓글 등록 성공"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(ApiResponse.fail("403", "대댓글 권한이 없습니다"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(ApiResponse.fail("500", "대댓글 등록 실패"));
        }
    }

    
    @GetMapping("/{review_no}")
    public ResponseEntity<ApiResponse<SitterReviewVO>> getReview(@PathVariable int review_no) {
        SitterReviewVO vo = reviewService.selectReviewById(review_no);
        if (vo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(ApiResponse.fail("404", "해당 리뷰가 존재하지 않습니다"));
        }
        return ResponseEntity.ok(ApiResponse.success(vo));
    }
    
    @GetMapping("/list_vue")
    public ResponseEntity<ApiResponse<List<SitterReviewVO>>> reviewList(@RequestParam("sitter_no") int sitter_no) {
        List<SitterReviewVO> list = reviewService.reviewList(sitter_no);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteReview(@RequestParam("review_no") int review_no,
                                                            @CookieValue(name = "accessToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("401", "인증 실패"));
        }

        try {
            int user_no = Integer.parseInt(jwt.getUserNoFromToken(token));
            reviewService.deleteReview(review_no, user_no);
            return ResponseEntity.ok(ApiResponse.success("삭제 성공"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(ApiResponse.fail("403", "삭제 권한 없음"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail("500", "삭제 실패"));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateReview(@RequestBody SitterReviewVO vo,
                                                            @CookieValue(name = "accessToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body(ApiResponse.fail("401", "인증 실패"));
        }

        try {
            int user_no = Integer.parseInt(jwt.getUserNoFromToken(token));
            reviewService.updateReview(vo, user_no);
            return ResponseEntity.ok(ApiResponse.success("수정 성공"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(ApiResponse.fail("403", "수정 권한 없음"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.fail("500", "수정 실패"));
        }
    }
}
