package com.sist.web.groupchat.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;

public interface GroupChatMapper {
	@Insert("INSERT INTO p_group_msg (message_no, group_no, sender_no, content, sender_nickname) "
		  + "VALUES (p_gm_mno_seq.nextval, #{group_no}, #{sender_no}, #{content}, #{sender_nickname})")
	@SelectKey(statement = "SELECT p_gm_mno_seq.currval FROM dual", keyProperty = "message_no", before = false, resultType = Long.class)
	public void insertGroupChatMessage(GroupChatDTO dto);
	
	public List<GroupChatDTO> selectLatestMessageByGroupNo(@Param("group_no") int groupNo, @Param("last_message_no") Long lastMessageNo);
	
	@Select("SELECT * FROM p_group_msg WHERE message_no = #{message_no}")
	public GroupChatDTO selectGroupChatByMessageNo(@Param("message_no") long messageNo);
	
	public List<GroupChatDTO> selectMessagesByFilters(MessageSearchFilterDTO dto);
	
	public List<GroupChatDTO> selectMessagesAround(Map<String, Object> param);
	
	@Select("SELECT nickname FROM p_group_member WHERE group_no = #{groupNo} AND user_no = #{userNo}")
	public String selectGroupNickname(@Param("groupNo") int groupNo, @Param("userNo") int userNo);
	
	@Select("SELECT MAX(message_no) FROM p_group_msg WHERE group_no = #{groupNo}")
	public Long selectLatestMessageNo(@Param("groupNo") int groupNo);
	
	@Select("SELECT user_no "
		  + "FROM p_group_member "
		  + "WHERE group_no = #{groupNo} "
		  + "AND viewing = 0 "
		  + "AND (last_read_message IS NULL OR last_read_message < #{messageNo}) "
		  + "AND user_no <> #{senderNo}")
	List<Long> selectUserToNotify(@Param("groupNo") int groupNo, @Param("messageNo") Long messageNo, @Param("senderNo") int senderNo);
}
