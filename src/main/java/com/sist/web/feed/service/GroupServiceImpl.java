package com.sist.web.feed.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sist.web.feed.dao.*;
import com.sist.web.feed.vo.*;

@Service
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupDAO dao;
	
	@Override
	public List<GroupVO> groupListData() {
		// TODO Auto-generated method stub
		return dao.groupListData();
	}

	@Override
	public GroupVO groupDetailData(int group_no) {
		// TODO Auto-generated method stub
		return dao.groupDetailData(group_no);
	}
	
	@Override
	public List<FeedVO> feedListData(int group_no) {
		// TODO Auto-generated method stub
		
		
		return dao.feedListData(group_no);
	}

	@Override
	public List<FeedFileInfoVO> fileListData(int no) {
		// TODO Auto-generated method stub
		
		
		return dao.fileListData(no);
	}

	@Override
	public Map groupFeedData(int group_no)
	{
		Map map = new HashMap();
		GroupVO gvo = dao.groupDetailData(group_no);
		
		List<FeedVO> feedList = dao.feedListData(group_no);
		
		for(FeedVO vo : feedList)
		{
			List<FeedFileInfoVO> flist = dao.fileListData(vo.getFeed_no());
			List<String> filenames = new ArrayList<String>();
			for(FeedFileInfoVO ffvo : flist)
			{
				filenames.add(ffvo.getFilename());
			}
			vo.setImages(filenames);	
			/*
			List<String> filenames = fvo.stream()
				    .map(FeedFileInfoVO::getFilename)
				    .collect(Collectors.toList());
			*/
			//stream으로 하면 코드는 간편한데 아직 공부못한부분
		}
		map.put("list", feedList);
		map.put("gvo", gvo);
		
		return map;
		
	}

	@Override
	public int feedInsertData(FeedVO vo) {
		// TODO Auto-generated method stub
		return dao.feedInsertData(vo);
		
	}


	@Override
	public void feedFileInsert(FeedFileInfoVO vo) {
		// TODO Auto-generated method stub
		dao.feedFileInsert(vo);
	}

	@Override
	public FeedVO feedDetailData(int feed_no) {
		// TODO Auto-generated method stub
		
		
		return dao.feedDetailData(feed_no);
	}
	
	@Override
	public Map feedData(int feed_no)
	{
		Map map = new HashMap();
		FeedVO vo = dao.feedDetailData(feed_no);
		List<FeedFileInfoVO> flist = dao.fileListData(vo.getFeed_no());
		List<String> filenames = new ArrayList<String>();
		for(FeedFileInfoVO ffvo : flist)
		{
			filenames.add(ffvo.getFilename());
		}
		vo.setImages(filenames);
		
		map.put("vo", vo);
		return map;	
	}

	@Override
	public int groupInsertData(GroupVO vo) {
		// TODO Auto-generated method stub
		return dao.groupInsertData(vo);
	}
	

}
