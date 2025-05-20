package com.sist.web.groupchat.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.groupchat.dao.GroupChatDAO;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;
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
	
	@Value("${aws.url}")
	private String a3BaseUrl;
	
	@Transactional
	@Override
	public void saveAndSendGroupChatMessage(GroupChatDTO vo) {
		
		vo.setSender_nickname(cDao.selectGroupNickname(vo.getGroup_no(), vo.getSender_no()));
		
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
		List<GroupChatDTO> list;
		try {
			list = cDao.selectLatestMessageByGroupNo(groupNo, lastMessageNo);
			
			if (list == null || list.isEmpty()) {
				throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
				// 채팅 내역 비어있을 시 분기 작성 필요.
			}
			
			for (GroupChatDTO dto : list) {
				if (dto.getProfile() != null && !dto.getProfile().isBlank()) {
					dto.setProfile(a3BaseUrl + dto.getProfile());
				}
			}
		} catch (Exception ex) {
			log.error("이미지 불러오기 실패", ex);
			throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
		}
		 
		Collections.reverse(list);
		return list;
	}
	
	@Override
	public List<GroupChatDTO> getMessagesByFilters(MessageSearchFilterDTO dto) {
		
		return cDao.selectMessagesByFilters(dto);
	}
	
	@Override
	public List<GroupChatDTO> getMessagesAround(int groupNo, int messageNo) {
		Map<String, Object> param = new HashMap<>();
		param.put("groupNo", groupNo);
		param.put("messageNo", messageNo);
		return cDao.selectMessagesAround(param);
	}
}
