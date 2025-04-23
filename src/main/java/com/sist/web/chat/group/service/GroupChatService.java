package com.sist.web.chat.group.service;

import java.util.List;

import com.sist.web.chat.group.vo.*;

public interface GroupChatService {
	void saveAndSendGroupChatMessage(GroupChatVO vo);
	List<GroupChatVO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo);
	void createGroup(GroupVO vo);
	void addGroupMember(GroupMemberVO vo);
	List<GroupVO> getGroupAll(String userId);
}
