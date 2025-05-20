package com.sist.web.feed.controller;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/groups")
public class GroupFeedRestController {
	@Autowired
	private AwsS3Service service2;
	
	private final GroupFeedService service;
	/*
	@GetMapping("group/groups")
	public ResponseEntity<Map> group_groups()
	{
		Map map = new HashMap<>();
		try {
			List<GroupVO> list = service.groupListData();
			map.put("list", list);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("group_vue 완료");
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	*/
	@GetMapping("/{group_no}/feeds")
	public ResponseEntity<Map> group_feeds(@PathVariable("group_no") int group_no, int page , HttpServletRequest request)
	{
		System.out.println("컨트롤러");
		Map map = new HashMap<>();
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
	
	
	@PostMapping("/feeds")
	public ResponseEntity<String> group_feeds_insert(@RequestParam("title") String title, @RequestParam("content") String content,
		    @RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam("group_no") int group_no, 
		    HttpServletRequest request)
	{
		String result="";
		try {
			long user_no=(long)request.getAttribute("userno");
			FeedVO vo = new FeedVO();
			System.out.println("입력된 title은 ="+title);
			System.out.println("입력된 content은 ="+content);
			System.out.println("입력된 files는 ="+files);
			System.out.println("입력된 group_no는 "+group_no);
			
			int fileCount = (files == null || files.isEmpty()) ? 0 : files.size();
			vo.setTitle(title);
	        vo.setContent(content);
	        vo.setFilecount(fileCount);
	        vo.setGroup_no(group_no);    
	        vo.setUser_no(user_no);   
			String path="c:\\download\\";
			try {
				
				int no=service.feedInsertData(vo);
				System.out.println("입력된 새글의 번호"+no);
				
				if(files != null && !files.isEmpty())
				{
					for (MultipartFile mf : files) {
		                String storedFileName = service2.uploadFile(mf, "feeds/");

		                FeedFileInfoVO fvo = new FeedFileInfoVO();
		                fvo.setFilename(storedFileName);       // S3 저장 경로
		                fvo.setFilesize(mf.getSize());          // 용량
		                fvo.setFeed_no(no);

		                service.feedFileInsert(fvo);
		            }
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			result="ok";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	
	@GetMapping("feed/comments")
	public ResponseEntity<Map> feed_comments_list(int page, int feed_no)
	{
		System.out.println("댓글리스트 restcontroller");
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
	
	@PostMapping("feed/comments")
	public ResponseEntity<Map> feed_comment_insert(int feed_no, FeedCommentVO vo, HttpServletRequest request)
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
	@PostMapping("feed/comments_update")
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
	
	@PostMapping("feed/comments_delete")
	public ResponseEntity<Map> feed_comments_delete(FeedCommentVO vo)
	{
		Map map = new HashedMap();
		try {
			map = service.feedCommentDeleteData(vo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map>(map, HttpStatus.OK);
	}
	
}
