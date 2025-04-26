package com.sist.web.feed.mapper;
import java.util.*;

import com.sist.web.feed.vo.*;
import org.apache.ibatis.annotations.Select;

public interface GroupMapper {
	@Select("SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, rownum as num "
			+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at "
			+ "FROM p_group ORDER BY group_no DESC))")
	public List<GroupVO> groupListData();
	
}

