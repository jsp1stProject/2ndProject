package com.sist.web.groupchat.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import com.sist.web.groupchat.dto.GroupChatDTO;

public interface GroupChatMapper {
	@Insert("INSERT INTO p_group_msg (message_no, group_no, sender_no, content, sender_nickname) "
		  + "VALUES (p_gm_mno_seq.nextval, #{group_no}, #{sender_no}, #{content}, #{sender_nickname})")
	@SelectKey(statement = "SELECT p_gm_mno_seq.currval FROM dual", keyProperty = "message_no", before = false, resultType = Long.class)
	public void insertGroupChatMessage(GroupChatDTO dto);
	
	public List<GroupChatDTO> selectLatestMessageByGroupNo(@Param("group_no") int groupNo, @Param("last_message_no") Long lastMessageNo);
	
	@Select("SELECT * FROM p_group_msg WHERE message_no = #{message_no}")
	public GroupChatDTO selectGroupChatByMessageNo(@Param("message_no") long messageNo);
	
	
	
	
	
	
	
	
	
	
}
