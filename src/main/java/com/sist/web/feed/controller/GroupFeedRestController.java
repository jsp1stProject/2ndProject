package com.sist.web.feed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.feed.service.*;
import com.sist.web.feed.vo.*;

import java.io.File;
import java.util.*;

@RestController
public class GroupFeedRestController {
	
	@Autowired
	private GroupService service;
	
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
	
	@GetMapping("group/feeds")
	public ResponseEntity<Map> group_feeds(int group_no)
	{
		Map map = new HashMap<>();
		try {
			
			map = service.groupFeedData(group_no);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("======== error ========");
			return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("feed_vue 완료");
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@PostMapping("group/feeds")
	public ResponseEntity<String> group_feeds_insert(@RequestParam("title") String title, @RequestParam("content") String content,
		    @RequestParam(value = "files", required = false) List<MultipartFile> files, @RequestParam("group_no") int group_no)
	{
		String result="";
		try {
			FeedVO vo = new FeedVO();
			System.out.println("입력된 title은 ="+title);
			System.out.println("입력된 content은 ="+content);
			System.out.println("입력된 files는 ="+files);
			System.out.println("입력된 group_no는 "+group_no);
			//System.out.println("vo데이터들은 ="+vo);
			//List<MultipartFile> list = vo.getFiles();
			//System.out.println("전송된 파일 수: "+list.size());
			//System.out.println(list);
			int fileCount = (files == null || files.isEmpty()) ? 0 : files.size();
			vo.setTitle(title);
	        vo.setContent(content);
	        vo.setFilecount(fileCount);
	        vo.setGroup_no(group_no);         // 추후 그룹번호 값으로 교체
	        vo.setUser_no(17);     // 추후 로그인 값으로 교체
			String path="c:\\download\\";
			try {
				
				int no=service.feedInsertData(vo);
				System.out.println("입력된 새글의 번호"+no);
				
				FeedFileInfoVO fvo = new FeedFileInfoVO();
				if(files != null && !files.isEmpty())
				{
					for(MultipartFile mf:files)
					{
						String filename=mf.getOriginalFilename();
						File file=new File(path+filename);
						mf.transferTo(file); //업로드
						
						fvo.setFilename(filename);
						fvo.setFilesize(file.length());
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

	
}
