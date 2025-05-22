package com.sist.web.sitter.controller;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.sitter.service.SitterService;
import com.sist.web.sitter.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.*;

@RestController
public class SitterRestController {

    @Autowired
    private SitterService service;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AwsS3Service AwsS3Service;

    private Integer validateTokenAndGetUserNo(String token) {
        if (token == null || token.trim().isEmpty() || !token.contains(".")) {
            throw new RuntimeException("UNAUTHORIZED");
        }
        return Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
    }

    @GetMapping("/sitter/list_vue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sitter_list(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "care_loc") String fd,
                                                                        @RequestParam(required = false) String st) {
        Map<String, Object> queryMap = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        int rowSize = 9;
        int start = (page - 1) * rowSize + 1;
        int end = page * rowSize;

        queryMap.put("start", start);
        queryMap.put("end", end);

        List<SitterVO> list;
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
        int endPage = Math.min(startPage + BLOCK - 1, totalpage);

        result.put("list", list);
        result.put("curpage", page);
        result.put("totalpage", totalpage);
        result.put("startPage", startPage);
        result.put("endPage", endPage);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/sitter/detail_vue")
    public ResponseEntity<ApiResponse<Map<String, Object>>> sitter_detail(
            @RequestParam("sitter_no") int sitter_no,
            @CookieValue(name = "accessToken", required = false) String token) {
        try {
            SitterVO vo = service.sitterDetailData(sitter_no);
            if (vo == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("404", "해당 펫시터를 찾을 수 없음"));

            int myUserNo = -1;
            if (token != null && token.contains(".")) {
                myUserNo = Integer.parseInt(jwtTokenProvider.getUserNoFromToken(token));
            }

            Map<String, Object> result = new HashMap<>();
            result.put("sitter", vo);
            result.put("myUserNo", myUserNo);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("500", "서버 오류"));
        }
    }


    @GetMapping("/sitter/jjim/list")
    public ResponseEntity<ApiResponse<List<SitterVO>>> jjimList(@CookieValue(value = "accessToken", required = false) String token) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            List<SitterVO> list = service.jjimSitterList(user_no);
            return ResponseEntity.ok(ApiResponse.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "인증 실패"));
        }
    }

    @PostMapping("/sitter/jjim/toggle")
    public ResponseEntity<ApiResponse<String>> toggleJjim(@CookieValue(value = "accessToken", required = false) String token,
                                                           @RequestBody Map<String, Integer> data) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            int sitter_no = data.get("sitter_no");
            boolean result = service.toggleJjim(user_no, sitter_no);
            return ResponseEntity.ok(ApiResponse.success(result ? "찜 추가됨" : "찜 취소됨"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "토큰 오류"));
        }
    }

    @PostMapping("/sitter/insert")
    public ResponseEntity<ApiResponse<String>> sitter_insert(@CookieValue(value = "accessToken", required = false) String token,
                                                             @RequestParam("upload") MultipartFile file,
                                                             @RequestParam("tag") String tag,
                                                             @RequestParam("content") String content,
                                                             @RequestParam("carecount") int carecount,
                                                             @RequestParam("care_loc") String care_loc,
                                                             @RequestParam("pet_first_price") String pet_first_price) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            if (!service.isSitter(user_no)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "펫시터만 글 작성 가능"));
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savePath = Paths.get("C:/upload/" + fileName);
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            SitterVO vo = new SitterVO();
            vo.setUser_no(user_no);
            vo.setTag(tag);
            vo.setContent(content);
            vo.setCarecount(carecount);
            vo.setCare_loc(care_loc);
            vo.setPet_first_price(pet_first_price);
            vo.setSitter_pic("/upload/" + fileName);

            service.sitterInsert(vo);
            return ResponseEntity.ok(ApiResponse.success("펫시터 등록 성공"));

        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "인증 오류"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "펫시터 등록 실패"));
        }
    }

    @PostMapping("/sitter/update")
    public ResponseEntity<ApiResponse<String>> sitter_update(@CookieValue(value = "accessToken", required = false) String token,
                                                             @RequestBody SitterVO vo) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            SitterVO dbVO = service.sitterDetailData(vo.getSitter_no());
            if (dbVO == null || dbVO.getUser_no() != user_no) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "수정 권한 없음"));
            }

            vo.setUser_no(user_no);
            service.sitterUpdate(vo);
            return ResponseEntity.ok(ApiResponse.success("펫시터 수정 성공"));

        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "인증 오류"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "펫시터 수정 실패"));
        }
    }

    @DeleteMapping("/sitter/delete")
    public ResponseEntity<ApiResponse<String>> sitter_delete(@CookieValue(value = "accessToken", required = false) String token,
                                                             @RequestParam int sitter_no) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            SitterVO dbVO = service.sitterDetailData(sitter_no);
            if (dbVO == null || dbVO.getUser_no() != user_no) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "삭제 권한 없음"));
            }

            service.deleteSitterReviewWithPost(sitter_no);
            service.sitterDelete(sitter_no);
            return ResponseEntity.ok(ApiResponse.success("펫시터 삭제 성공"));

        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "인증 오류"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "펫시터 삭제 실패"));
        }
    }

    @GetMapping("/sitter/review")
    public ResponseEntity<ApiResponse<List<SitterReviewVO>>> review_list(@RequestParam int sitter_no) {
        try {
            List<SitterReviewVO> list = service.reviewListData(sitter_no);
            return ResponseEntity.ok(ApiResponse.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "리뷰 목록 조회 실패"));
        }
    }

    @PostMapping("/sitter/review")
    public ResponseEntity<ApiResponse<String>> review_insert(@RequestBody SitterReviewVO vo) {
        try {
        	
            service.reviewInsert(vo);
            return ResponseEntity.ok(ApiResponse.success("리뷰 등록 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "리뷰 등록 실패"));
        }
    }

    @PostMapping("/sitter/review/reply")
    public ResponseEntity<ApiResponse<String>> reply_insert(@RequestBody SitterReviewVO vo,
                                                            @CookieValue(value = "accessToken", required = false) String token) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            SitterVO sitterVO = service.sitterDetailData(vo.getSitter_no());
            if (sitterVO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail("404", "해당 게시글이 존재하지 않습니다"));
            }
            if (sitterVO.getUser_no() != user_no) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "대댓글 권한이 없습니다"));
            }
            vo.setUser_no(user_no);
            service.replyInsert(vo);
            return ResponseEntity.ok(ApiResponse.success("대댓글 작성 성공"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("401", "토큰 오류"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "대댓글 작성 실패"));
        }
    }

    @PutMapping("/sitter/review")
    public ResponseEntity<ApiResponse<String>> review_update(@RequestBody SitterReviewVO vo,
                                                             @CookieValue(value = "accessToken", required = false) String token) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            int writer_no = service.getReviewWriter(vo.getReview_no());
            if (writer_no != user_no) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "수정 권한 없음"));
            }
            service.reviewUpdate(vo);
            return ResponseEntity.ok(ApiResponse.success("리뷰 수정 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "리뷰 수정 실패"));
        }
    }

    @DeleteMapping("/sitter/review")
    public ResponseEntity<ApiResponse<String>> review_delete(@RequestParam int review_no,
                                                             @CookieValue(value = "accessToken", required = false) String token) {
        try {
            int user_no = validateTokenAndGetUserNo(token);
            int writer_no = service.getReviewWriter(review_no);
            if (writer_no != user_no) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail("403", "삭제 권한 없음"));
            }
            service.reviewDelete(review_no);
            return ResponseEntity.ok(ApiResponse.success("리뷰 삭제 성공"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("500", "리뷰 삭제 실패"));
        }
    }
} 