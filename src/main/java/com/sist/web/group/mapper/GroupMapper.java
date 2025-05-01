package com.sist.web.group.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import com.sist.web.group.dto.GroupDTO;

public interface GroupMapper {
		//그룹
		@Select("SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, num "
				+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at, rownum as num "
				+ "FROM (SELECT group_no, group_name, profile_img, description, capacity, is_public, owner,created_at "
				+ "FROM p_group ORDER BY group_no DESC))")
		public List<GroupDTO> selectGroupAllList();
		
		@Select("SELECT * FROM p_group WHERE group_no=#{group_no}")
		public GroupDTO selectGroupDetail(int group_no);
		
		// 동적 쿼리 작성 예정
		public void groupInsertData(GroupDTO vo);
		
		@Select("SELECT p_group_no_seq.currval FROM DUAL")
		public int groupCurrentNodata();
		
		
		
}
