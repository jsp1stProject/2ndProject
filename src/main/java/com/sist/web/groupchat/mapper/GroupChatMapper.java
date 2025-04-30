package com.sist.web.groupchat.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;

public interface GroupChatMapper {
	@Insert("INSERT INTO p_group_msg (message_no, group_no, sender_no, content, sender_nickname) "
		  + "VALUES (p_gm_mno_seq.nextval, #{group_no}, #{sender_no}, #{content}, #{sender_nickname})")
	@SelectKey(statement = "SELECT p_gm_mno_seq.currval FROM dual", keyProperty = "message_no", before = false, resultType = Long.class)
	public void insertGroupChatMessage(GroupChatDTO vo);
	
	public List<GroupChatDTO> selectLatestMessageByGroupNo(@Param("group_no") int groupNo, @Param("last_message_no") Long lastMessageNo);
	
	@Select("SELECT * FROM p_group_msg WHERE message_no = #{message_no}")
	public GroupChatDTO selectGroupChatByMessageNo(@Param("message_no") long messageNo);
	
	@Insert("INSERT INTO p_group (group_no, group_name, description, owner) "
		  + "VALUES(p_group_no_seq.nextval, #{group_name}, #{description}, #{owner})")
	@SelectKey(statement = "SELECT p_group_no_seq.currval FROM dual", keyProperty = "group_no", before = false, resultType = Integer.class)
	public void insertGroup(GroupDTO vo);
	
	public void insertGroupMember(GroupMemberDTO vo);
	
	@Select("SELECT g.group_no, group_name FROM p_group g JOIN p_group_member m ON g.group_no = m.group_no WHERE m.user_no = #{user_no}")
	public List<GroupDTO> selectGroup(String userNo);
	
	@Select("SELECT group_no, user_no, nickname FROM p_group_member WHERE group_no = #{group_no}")
	public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo);
}
