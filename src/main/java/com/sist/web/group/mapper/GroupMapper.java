package com.sist.web.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.dto.GroupMemberDTO;
import com.sist.web.group.dto.GroupMemberInfoDTO;

public interface GroupMapper {
		//그룹
		@Select("SELECT g.group_no, g.group_name, g.profile_img, g.description, g.capacity, g.is_public, g.created_at, u.nickname as owner_name, "
				+ "(SELECT COUNT(*) FROM p_group_member gm WHERE gm.group_no=g.group_no AND left_at IS NULL) as current_member_count "
				+ "FROM p_group g "
				+ "JOIN p_users u "
				+ "ON g.owner=u.user_no "
				+ "ORDER BY g.created_at DESC")
		public List<GroupDTO> selectGroupAllList();
		
		public List<Map<String, Object>> selectGroupMemberStates(int user_no);
		
		@Select("SELECT * FROM p_group WHERE group_no = #{group_no}")
		public GroupDTO selectGroupDetail(@Param("group_no") int groupNo);
		
		@Select("SELECT g.group_no, g.group_name, g.profile_img, g.description, g.capacity, g.is_public, g.owner, g.created_at, u.nickname as owner_name, "
				+ "(SELECT COUNT(*) FROM p_group_member gm WHERE gm.group_no=g.group_no AND left_at IS NULL) as current_member_count "
				+ "FROM p_group g "
				+ "JOIN p_users u "
				+ "ON g.owner=u.user_no "
				+ "WHERE group_no = #{group_no}")
		public GroupDTO selectGroupDetailTotal(@Param("group_no") int groupNo);
		
		// 동적 쿼리 작성 예정
		public void groupInsertData(GroupDTO dto);
		
		@Select("SELECT p_group_no_seq.currval FROM DUAL")
		public int groupCurrentNodata();
		
		public void insertGroup(GroupDTO dto);
		
		public void insertGroupMember(GroupMemberDTO dto);
		
		@Select("SELECT g.group_no, group_name, g.profile_img FROM p_group g JOIN p_group_member m ON g.group_no = m.group_no WHERE m.user_no = #{user_no}")
		public List<GroupDTO> selectGroup(String userNo);
		
		@Select("SELECT gm.group_no, gm.user_no, gm.nickname, u.profile "
			  + "FROM p_group_member gm "
			  + "JOIN p_users u "
			  + "ON gm.user_no = u.user_no "
			  + "WHERE gm.group_no = #{group_no}")
		public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo);
		
		@Insert("INSERT INTO p_group_joinrequests(request_no, group_no, user_no) VALUES(p_gjoin_req_seq.nextval, #{group_no}, #{user_no} )")
		public void insertJoinRequests(GroupJoinRequestsDTO dto);
		
		@Select("SELECT r.request_no,r.group_no,r.user_no,r.status, TO_CHAR(r.request_date, 'YYYY-MM-DD HH24:MI:SS') as dbday,(SELECT nickname FROM p_users u WHERE r.user_no=u.user_no) as user_nickname "
				+ "FROM p_group_joinrequests r "
				+ "JOIN p_group u "
				+ "ON r.group_no=u.group_no "
				+ "WHERE u.owner=#{user_no} "
				+ "ORDER BY r.request_date DESC")
		public List<GroupJoinRequestsDTO> selectGroupRequestsData(int user_no);
		
		@Update("UPDATE p_group_joinrequests SET status=#{status} WHERE request_no=#{request_no}")
		public void updateJoinRequestStatus(GroupJoinRequestsDTO dto);
		
		public void updateGroupDetail(GroupDTO dto);
		
		public void insertGroupTags(Map<String, Object> tags);
		
		@Delete("DELETE p_group_tag WHERE group_no = #{group_no}")
		public void deleteGroupTags(@Param("group_no") int groupNo);
		
		@Delete("DELETE p_group WHERE group_no = #{group_no}")
		public void deleteGroup(@Param("group_no") int groupNo);
		
		@Select("SELECT COUNT(*) FROM p_group_member WHERE group_no = #{group_no}")
		public int selectMemberCountByGroupNo(@Param("group_no") int groupNo);
		
		@Select("SELECT g.group_no, g.nickname, g.joined_at, g.role, u.profile, g.user_no "
			  + "FROM p_group_member g JOIN p_users u "
			  + "ON g.user_no = u.user_no "
			  + "WHERE g.group_no = #{group_no} "
			  + "AND u.user_no = #{user_no}")
		@Results(
				{@Result(property = "joinedAt", column = "joined_at"),
				@Result(property = "userNo", column = "user_no")})
		public GroupMemberInfoDTO selectGroupMemberInfo(@Param("group_no") int groupNo, @Param("user_no") int userNo);
		
		@Update("UPDATE p_group_member SET viewing = #{viewing} WHERE group_no = #{groupNo} AND user_no = #{userNo}")
		public void updateViewingStatus(@Param("groupNo") int groupNo, @Param("userNo") int userNo, @Param("viewing") int viewing);
		
		@Update("UPDATE p_group_member SET viewing = 0, last_read_message = #{lastReadMessageNo} WHERE group_no = #{groupNo} AND user_no = #{userNo}")
		public void updateExitStatus(@Param("groupNo") int groupNo, @Param("userNo") int userNo, @Param("lastReadMessageNo") Long lastReadMessageNo);
		
		@Update("UPDATE p_group_member SET last_seen_at = SYSTIMESTAMP WHERE user_no = #{userNo}")
		public void updateLastSeenAt(@Param("userNo") int userNo);
		
		@Update("UPDATE p_group_member SET viewing = 0 WHERE viewing = 1 AND last_seen_at < SYSTIMESTAMP - INTERVAL '1' MINUTE")
		public void markInactiveUsers();
		
		@Update("UPDATE p_group_member SET viewing = 0 WHERE user_no = #{userNo}")
		public void updateViewingZero(@Param("userNo") int userNo);
		
		@Select("SELECT tag FROM p_group_tag WHERE group_no = #{groupNo}")
		public List<String> selectGroupTagsByGroupNo(@Param("groupNo") int groupNo);
		
		@Update("UPDATE p_group_member SET nickname = #{nickname} WHERE user_no = #{userNo}")
		public void updateGroupMemberNickname(@Param("userNo") int userNo, @Param("nickname") String nickname);
		
}
