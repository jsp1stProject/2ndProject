package com.sist.web.feed.service;

import java.util.List;

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

}
