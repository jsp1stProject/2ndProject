package com.sist.web.chat.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import com.sist.web.chat.group.vo.*;

public interface GroupChatMapper {
	@Insert("INSERT INTO group_msg (message_id, group_id, sender_id, content) "
		  + "VALUES (gm_mid_seq.nextval, #{group_id}, #{sender_id}, #{content})")
	public void insertGroupChatMessage(GroupChatVO vo);
	
	public List<GroupChatVO> selectLatestMessageByGroupId(long groupId, long lastMessageId);
	
	@Insert("INSERT INTO p_group (group_id, group_name, description, created_by) "
		  + "VALUES(gr_gid_seq.nextval, #{group_name}, #{description}, #{created_by})")
	public void insertGroup(GroupVO vo);
	
	@Insert("INSERT INTO group_member (group_id, user_id, nickname) "
			+ "VALUES(#{group_id}, #{user_id}, #{nicekname})")
	@SelectKey(statement = "SELECT gr_gid_seq.currval FROM dual", keyProperty = "group_id", before = false, resultType = Long.class)
	public void insertGroupMember(GroupMemberVO vo);
	
	@Select("SELECT g.group_id, group_name FROM p_group g JOIN group_member m ON g.group_id = m.group_id WHERE m.user_id = #{user_id}")
	public List<GroupVO> selectGroup(String userId);
}
