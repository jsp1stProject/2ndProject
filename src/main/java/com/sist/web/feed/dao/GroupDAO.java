package com.sist.web.feed.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.sist.web.feed.vo.*;
import com.sist.web.feed.mapper.*;

@Repository
public class GroupDAO {

	@Autowired
	private GroupMapper mapper;
	
	public List<GroupVO> groupListData()
	{
		return mapper.groupListData();
	}
	
	public GroupVO groupDetailData(int group_no)
	{
		return mapper.groupDetailData(group_no);
	}
	
	public List<FeedVO> feedListData(int group_no)
	{
		return mapper.feedListData(group_no);
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
	
	public FeedVO feedDetailData(int feed_no)
	{
		return mapper.feedDetailData(feed_no);
	}
}
