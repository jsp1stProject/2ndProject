package com.sist.web.chat.group.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sist.web.chat.group.controller.*;
import com.sist.web.chat.group.dao.*;
import com.sist.web.chat.group.vo.*;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {

	private final GroupChatDAO cDao;
	private final SimpMessagingTemplate messagingTemplate;
	private final UserMapper userMapper;
	@Transactional
	@Override
	public void saveAndSendGroupChatMessage(GroupChatVO vo) {
		
		UserVO sender = userMapper.getUserMailFromUserNo(String.valueOf(vo.getSender_no()));
		vo.setSender_nickname(sender.getNickname());
		
		cDao.insertGroupChatMessage(vo);
		log.info("메세지 전송 성공: {}", vo.getContent());
		
		try {
			messagingTemplate.convertAndSend("/sub/chats/groups/" + vo.getGroup_no(), vo);
			log.info("STOMP 연결 성공: {}", vo.getContent());
		} catch (Exception ex) {
			log.error("STOMP 연결 실패: {}}", vo.getGroup_no(), ex.getMessage());
			throw new RuntimeException("STOMP 전송 실패", ex);
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
		// Group_no, user_no, nickname
		member.setGroup_no(vo.getGroup_no());
		member.setUser_no(vo.getOwner());
		member.setRole("OWNER");
		
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
