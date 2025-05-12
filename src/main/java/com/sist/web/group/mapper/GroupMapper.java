package com.sist.web.group.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.dto.GroupMemberDTO;

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
		public void groupInsertData(GroupDTO dto);
		
		@Select("SELECT p_group_no_seq.currval FROM DUAL")
		public int groupCurrentNodata();
		
		@Insert("INSERT INTO p_group (group_no, group_name, description, owner) "
				+ "VALUES(p_group_no_seq.nextval, #{group_name}, #{description}, #{owner})")
		@SelectKey(statement = "SELECT p_group_no_seq.currval FROM dual", keyProperty = "group_no", before = false, resultType = Integer.class)
		public void insertGroup(GroupDTO dto);
		
		public void insertGroupMember(GroupMemberDTO dto);
		
		@Select("SELECT g.group_no, group_name FROM p_group g JOIN p_group_member m ON g.group_no = m.group_no WHERE m.user_no = #{user_no}")
		public List<GroupDTO> selectGroup(String userNo);
		
		@Select("SELECT group_no, user_no, nickname FROM p_group_member WHERE group_no = #{group_no}")
		public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo);
		
		@Insert("INSERT INTO p_group_joinrequest(request_no, group_no, user_no) VALUES(p_gjoin_req_seq.nextval(), #{group_no}, #{user_no} )")
		public void insertJoinRequests(GroupJoinRequestsDTO dto);
		
}
