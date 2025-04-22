package com.sist.web.chat.group.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import com.sist.web.chat.group.vo.*;

public interface GroupChatMapper {
	@Insert("INSERT INTO p_group_msg (message_no, group_no, sender_no, content) "
		  + "VALUES (p_gm_mno_seq.nextval, #{group_no}, #{sender_no}, #{content})")
	public void insertGroupChatMessage(GroupChatVO vo);
	
	public List<GroupChatVO> selectLatestMessageByGroupId(@Param("group_no") int groupId, @Param("last_message_no") Long lastMessageId);
	
	@Insert("INSERT INTO p_group (group_no, group_name, description, owner) "
		  + "VALUES(p_group_no_seq.nextval, #{group_name}, #{description}, #{owner})")
	@SelectKey(statement = "SELECT p_group_no_seq.currval FROM dual", keyProperty = "group_no", before = false, resultType = Integer.class)
	public void insertGroup(GroupVO vo);
	
	public void insertGroupMember(GroupMemberVO vo);
	
	@Select("SELECT g.group_no, group_name FROM p_group g JOIN p_group_member m ON g.group_no = m.group_no WHERE m.user_no = #{user_no}")
	public List<GroupVO> selectGroup(String userId);
}
