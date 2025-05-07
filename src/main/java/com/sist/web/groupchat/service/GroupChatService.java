package com.sist.web.groupchat.service;

import java.util.List;

import com.sist.web.groupchat.dto.GroupChatDTO;

public interface GroupChatService {
	void saveAndSendGroupChatMessage(GroupChatDTO vo);
	List<GroupChatDTO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo);
	
	
	
}
