package com.sist.web.groupchat.service;

import java.util.List;
import com.sist.web.groupchat.vo.GroupChatVO;
import com.sist.web.groupchat.vo.GroupMemberVO;
import com.sist.web.groupchat.vo.GroupVO;

public interface GroupChatService {
	void saveAndSendGroupChatMessage(GroupChatVO vo);
	List<GroupChatVO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo);
	void createGroup(GroupVO vo);
	void addGroupMember(GroupMemberVO vo);
	List<GroupVO> getGroupAll(String userId);
	List<GroupMemberVO> getGroupMemberAllByGroupNo(int groupNo);
}
