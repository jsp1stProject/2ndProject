package com.sist.web.groupchat.service;

import java.util.List;

import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;

public interface GroupChatService {
	void saveAndSendGroupChatMessage(GroupChatDTO vo);
	List<GroupChatDTO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo);
	
	
	
}
