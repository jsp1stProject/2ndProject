package com.sist.web.feed.service;

import java.util.*;
import com.sist.web.feed.vo.*;

public interface GroupService {
	
	public List<GroupVO> groupListData();	
	public GroupVO groupDetailData(int group_no);
	public List<FeedVO> feedListData(int group_no);
	public List<FeedFileInfoVO> fileListData(int feed_no);
	public Map groupFeedData(int group_no);
	public int feedInsertData(FeedVO vo);
	public void feedFileInsert(FeedFileInfoVO vo);
	public FeedVO feedDetailData(int feed_no);
	public Map feedData(int feed_no);
	public int groupInsertData(GroupVO vo);
	
	
}
