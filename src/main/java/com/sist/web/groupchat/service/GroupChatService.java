package com.sist.web.groupchat.service;

import java.util.List;
import java.util.Map;

import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;

public interface GroupChatService {
	void saveAndSendGroupChatMessage(GroupChatDTO vo);
	List<GroupChatDTO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo);
	List<GroupChatDTO> getMessagesByFilters(MessageSearchFilterDTO dto);
	List<GroupChatDTO> getMessagesAround(int groupNo, int messageNo);
	void markViewing(int groupNo, int userNo, boolean isViewing);
	void markExitAndUpdateLastRead(int groupNo, int userNo);
}
