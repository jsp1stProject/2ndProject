package com.sist.web.chat.group.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sist.web.chat.group.mapper.*;
import com.sist.web.chat.group.vo.*;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupChatDAO {
	private final GroupChatMapper mapper;
	
	/** �޼��� ���� */
	public void insertGroupChatMessage(GroupChatVO vo) {
		mapper.insertGroupChatMessage(vo);
	}
	
	/** �ֱ� �޼��� 20�� ��ȸ */
	public List<GroupChatVO> selectLatestMessageByGroupId(long groupId, long lastMessageId) {
		return mapper.selectLatestMessageByGroupId(groupId, lastMessageId);
	}
	
	/** �׷� ���� */
	public void insertGroup(GroupVO vo) {
		mapper.insertGroup(vo);
	}
	
	/** �׷�� �߰� */
	public void insertGroupMember(GroupMemberVO vo) {
		mapper.insertGroupMember(vo);
	}
	
	/** �׷� ��ȸ */
	public List<GroupVO> selectGroup(String userId) {
		return mapper.selectGroup(userId);
	}
}