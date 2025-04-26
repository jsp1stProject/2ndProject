package com.sist.web.feed.dao;

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
	
}
