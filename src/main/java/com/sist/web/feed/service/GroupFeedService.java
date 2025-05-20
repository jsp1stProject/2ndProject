package com.sist.web.feed.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.feed.vo.*;

public interface GroupFeedService {
	
	//public List<GroupVO> groupListData();	
	//public GroupVO groupDetailData(int group_no);
	public List<FeedVO> feedListData(Map map);
	public List<FeedFileInfoVO> fileListData(int feed_no);
	//public List<GroupMemberVO> joined_groupmember(int group_no);
	public Map groupFeedTotalData(int group_no, int page, long user_no);
	public int feedInsertData(FeedVO vo);
	public void feedFileInsert(FeedFileInfoVO vo);
	public String feedInserDataTotal(int group_no, long user_no, String title, String content, List<MultipartFile> files);
	public FeedVO feedDetailData(int feed_no);
	public FeedVO feedData(int feed_no);
	//public int groupInsertData(GroupVO vo);
	public List<FeedCommentVO> feedCommentListData(Map map);
	public int feedCommentTotalPage(int feed_no);
	public Map FeedCommentTotalList(int page, int feed_no);
	public void feedCommentInsert(FeedCommentVO vo);
	public Map feedCommentAdd(int feed_no, FeedCommentVO vo, HttpServletRequest request);
	public void feedCommentUpdate(@Param("msg") String msg, @Param("no") int no);	
	public Map feedCommentUpdateData(FeedCommentVO vo);
	public void feedCommentDelete(Map map);
	public Map feedCommentDeleteData(FeedCommentVO vo);
	
}
