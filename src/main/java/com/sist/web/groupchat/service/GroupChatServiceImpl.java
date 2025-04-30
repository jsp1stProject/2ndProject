package com.sist.web.groupchat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.groupchat.dao.GroupChatDAO;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;
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
	public void saveAndSendGroupChatMessage(GroupChatDTO vo) {
		
		UserVO sender = userMapper.getUserMailFromUserNo(String.valueOf(vo.getSender_no()));
		vo.setSender_nickname(sender.getNickname());
		
		cDao.insertGroupChatMessage(vo);
		log.info("메세지 저장 성공: {}", vo.getContent());
		GroupChatDTO saved = cDao.selectGroupChatByMessageNo(vo.getMessage_no());
		try {
			messagingTemplate.convertAndSend("/sub/chats/groups/" + saved.getGroup_no(), saved);
			log.info("STOMP 메세지 전송 성공 - groupNo: {}, content: {}", saved.getGroup_no(), saved.getContent());
		} catch (Exception ex) {
			log.error("STOMP 메세지 전송 실패 - groupNo: {}, error: {}", saved.getGroup_no(), ex.getMessage());
			throw new RuntimeException("STOMP 전송 실패", ex);
		}
	}

	@Override
	public List<GroupChatDTO> getLatestMessageByGroupNo(int groupNo, Long lastMessageNo) {
		List<GroupChatDTO> list = cDao.selectLatestMessageByGroupNo(groupNo, lastMessageNo);
		if (list == null || list.isEmpty()) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		Collections.reverse(list);
		return list;
	}
	
	@Transactional
	@Override
	public void createGroup(GroupDTO vo) {
		cDao.insertGroup(vo);
		
		GroupMemberDTO member = new GroupMemberDTO();
		// group_no, user_no, nickname
		member.setGroup_no(vo.getGroup_no());
		member.setUser_no(vo.getOwner());
		member.setRole("OWNER");
		
		cDao.insertGroupMember(member);
	}

	@Override
	public void addGroupMember(GroupMemberDTO vo) {
		cDao.insertGroupMember(vo);
	}

	@Override
	public List<GroupDTO> getGroupAll(String userNo) {
		if (userNo == null) {
	        throw new CommonException(CommonErrorCode.MISSING_PARAMETER);
	    }
	    return cDao.selectGroup(userNo);
	}

	@Override
	public List<GroupMemberDTO> getGroupMemberAllByGroupNo(int groupNo) {
		List<GroupMemberDTO> list = new ArrayList<GroupMemberDTO>();
		try {
			list = cDao.selectGroupMemberAllByGroupNo(groupNo);
		} catch (Exception ex) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		return list;
	}

}
