package com.sist.web.feed.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.sist.web.feed.vo.*;
import com.sist.web.feed.mapper.*;

@Repository
public class GroupFeedDAO {

	@Autowired
	private GroupFeedMapper mapper;
	/*
	public List<GroupVO> groupListData()
	{
		return mapper.groupListData();
	}

	public List<GroupMemberVO> joined_groupmember(int group_no)
	{
		return mapper.joined_groupmember(group_no);
	}
	
	public GroupVO groupDetailData(int group_no)
	{
		return mapper.groupDetailData(group_no);
	}
	*/
	public List<FeedVO> feedListData(Map map)
	{
		return mapper.feedListData(map);
	}
	
	public List<FeedFileInfoVO> fileListData(int feed_no)
	{
		return mapper.fileListData(feed_no);
	}
	
	public int feedInsertData(FeedVO vo)
	{
		mapper.feedInsertData(vo);
		return mapper.feedCurentNodata();
	}
	
	public void feedFileInsert(FeedFileInfoVO vo)
	{
		mapper.feedFileInsert(vo);;
	}
	
	public FeedVO feedDetailData(int feed_no, long user_no)
	{
		return mapper.feedDetailData(feed_no,user_no);
	}
	/*
	 * 
	 * 수정필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public int groupInsertData(GroupVO vo)
	{
		mapper.groupInsertData(vo);
		return mapper.groupCurentNodata();
	}
	*/
	public List<FeedCommentVO> feedCommentListData(Map map)
	{
		return mapper.feedCommentListData(map);
	}
	
	public int feedCommentTotalPage(int feed_no)
	{
		return mapper.feedCommentTotalPage(feed_no);
	}
	
	public void feedCommentInsert(FeedCommentVO vo)
	{
		mapper.feedCommentInsert(vo);
	}
	
	public void feedCommentUpdate(@Param("msg") String msg, @Param("no") int no)
	{
		mapper.feedCommentUpdate(msg, no);
	}
	
	public void feedCommentDelete(Map map)
	{
		mapper.feedCommentDelete(map);
	}
	
	public void feedReplyInsert	(FeedCommentVO vo)
	{
		mapper.feedReplyInsert(vo);
	}
	
	public int hasUserLike(@Param("user_no") long user_no, @Param("feed_no") int feed_no)
	{
		return mapper.hasUserLike(user_no, feed_no);
	}
	
	public void likeInsert(@Param("user_no") long user_no, @Param("feed_no") int feed_no)
	{
		mapper.likeInsert(user_no, feed_no);
	}
	
	public void likeDelete(@Param("user_no") long user_no, @Param("feed_no") int feed_no)
	{
		mapper.likeDelete(user_no, feed_no);
	}
}
