package com.sist.web.feed.controller;

import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.feed.service.*;
import com.sist.web.feed.vo.*;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class GroupFeedRestController {
	
	private final GroupFeedService service;
	//선택된 그룹페이지 안 리스트피드목록
	@GetMapping("/api/groups/{group_no}/feeds")
	public ResponseEntity<Map<String, Object>> group_feeds(@PathVariable("group_no") int group_no, int page , HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			long user_no = (long)request.getAttribute("userno");
			map = service.groupFeedTotalData(group_no, page,user_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("feed_vue 완료");
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

	// 피드 작성
	@PostMapping("/api/groups/{group_no}/feeds")
	public ResponseEntity<String> group_feeds_insert(@PathVariable("group_no") int group_no, @RequestParam("title") String title, @RequestParam("content") String content,
		    @RequestParam(value = "files", required = false) List<MultipartFile> files, 
		    HttpServletRequest request)
	{
		String result="";
		try {
			long user_no=(long)request.getAttribute("userno");
			result=service.feedInserDataTotal(group_no, user_no, title, content, files);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	// 피드 상세보기 
	@GetMapping("/api/feeds/{feed_no}")
	public ResponseEntity<FeedVO> feed_detail(@PathVariable("feed_no") int feed_no, HttpServletRequest request)
	{
		FeedVO vo = new FeedVO();
		try {
			long user_no = (long)request.getAttribute("userno");
			vo = service.feedData(feed_no,user_no);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(vo,HttpStatus.OK);
	}
	
	// 댓글 리스트
	@GetMapping("api/feeds/{feed_no}/comments")
	public ResponseEntity<Map> feed_comments_list(int page, @PathVariable("feed_no") int feed_no)
	{
		Map map = new HashedMap();
		try {
			map = service.FeedCommentTotalList(page, feed_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	//댓글쓰기
	@PostMapping("api/feed/{feed_no}/comments")
	public ResponseEntity<Map> feed_comment_insert(@PathVariable("feed_no") int feed_no, FeedCommentVO vo, HttpServletRequest request)
	{
		System.out.println("댓글쓰기 restcontroller");
		Map map = new HashedMap();
		try {
			map = service.feedCommentAdd(feed_no, vo, request);
			System.out.println("댓글쓰기 성공");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	/*
	 * @PostMapping("feed/update_comment") public ResponseEntity<Map>
	 * feed_comment_update(int feed_no, )
	 * 
	 */
	//댓글 수정
	@PutMapping("api/feed/comments/{feed_no}")
	public ResponseEntity<Map> feed_comments_update(FeedCommentVO vo)
	{
		Map map = new HashedMap();
		try {
			map = service.feedCommentUpdateData(vo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map>(map, HttpStatus.OK);
	}
	
	//댓글 삭제
	@DeleteMapping("api/feed/comments/{comment_no}")
	public ResponseEntity<Map> feed_comments_delete(@PathVariable("comment_no") int comment_no, @RequestParam("group_id") int group_id, @RequestParam("group_step") int group_step)
	{	
		System.out.println("삭제시작");
		Map map = new HashedMap();
		try {
			FeedCommentVO vo = new FeedCommentVO();
			vo.setNo(comment_no);
			vo.setGroup_id(group_id);
			vo.setGroup_step(group_step);
			map = service.feedCommentDeleteData(vo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map>(map, HttpStatus.OK);
	}
	
	//대댓글 작성
	@PostMapping("api/feed/reply/{comment_no}")
	public ResponseEntity<String> feed_reply_insert(@PathVariable("comment_no") int comment_no, HttpServletRequest request, 
					@RequestBody FeedCommentVO vo)
	{
		System.out.println("##############################대댓글 등록#######################");
		System.out.println(vo);
		
		String result="";
		try {
			long user_no=(long)request.getAttribute("userno");
			System.out.println(user_no);
			vo.setUser_no(user_no);
			System.out.println(vo);
			service.feedReplyInsert(vo);
			result="성공";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	@PostMapping("api/feed/{feed_no}/like")
	public ResponseEntity<String> selectLike(@PathVariable("feed_no") int feed_no, HttpServletRequest request)
	{
		String result ="";
		long user_no = (long)request.getAttribute("userno");
		System.out.println("#############################################");
		System.out.println(user_no);
		System.out.println(feed_no);
	    service.selectLike(user_no, feed_no);
	    return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
