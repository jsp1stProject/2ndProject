package com.sist.web.chat.group.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sist.web.chat.group.controller.*;
import com.sist.web.chat.group.dao.*;
import com.sist.web.chat.group.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {

	private final GroupChatDAO cDao;
	private final SimpMessagingTemplate messagingTemplate;
	
	@Transactional
	@Override
	public void saveAndSendGroupChatMessage(GroupChatVO vo) {
		if (vo.getSender_id() == null || vo.getSender_id().isBlank()) {
			log.debug("Sender_id is null: {}", vo.getSender_id());
			return;
		} else {
			cDao.insertGroupChatMessage(vo);
			log.info("�޽��� �����: {}", vo.getContent());
			
			try {
				messagingTemplate.convertAndSend("/sub/chats/groups/" + vo.getGroup_id(), vo);
				log.info("STOMP �޽��� ���۵�: {}", vo.getContent());
			} catch (Exception ex) {
				log.error("STOMP �޽��� ���� ����, �׷� ID: {}, ����: {}", vo.getGroup_id(), ex.getMessage());
				throw new RuntimeException("STOMP ���� ����", ex);
			}
		}
	}

	@Override
	public List<GroupChatVO> getLatestMessageByGroupId(int groupId, Long lastMessageId) {
		//List<GroupChatVO> list = cDao.selectLatestMessageByGroupId(groupId, lastMessageId);
		
		return cDao.selectLatestMessageByGroupId(groupId, lastMessageId);
	}
	
	@Transactional
	@Override
	public void createGroup(GroupVO vo) {
		cDao.insertGroup(vo);
		
		GroupMemberVO member = new GroupMemberVO();
		System.out.println("groupid: " + vo.getGroup_id());
		member.setGroup_id(vo.getGroup_id());
		member.setUser_id(vo.getCreated_by());
		member.setNickname(vo.getCreated_by());
		
		cDao.insertGroupMember(member);
	}

	@Override
	public void addGroupMember(GroupMemberVO vo) {
		cDao.insertGroupMember(vo);
	}

	@Override
	public List<GroupVO> getGroupAll(String userId) {
		List<GroupVO> list = cDao.selectGroup(userId);
		return list;
	}

}
