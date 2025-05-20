package com.sist.web.groupchat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.MessageSearchFilterDTO;
import com.sist.web.groupchat.mapper.GroupChatMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupChatDAO {
	private final GroupChatMapper mapper;
	
	/** 사용자가 전송한 메세지 DB 삽입 */
	public void insertGroupChatMessage(GroupChatDTO vo) {
		mapper.insertGroupChatMessage(vo);
	}
	/** 그룹 번호 기준으로 해당 그룹의 최근 메시지 20개 조회 */
	public List<GroupChatDTO> selectLatestMessageByGroupNo(int groupNo, Long lastMessageNo) {
		return mapper.selectLatestMessageByGroupNo(groupNo, lastMessageNo);
	}
	
	/** message_no 를 기준으로 해당 메세지의 모든 정보 조회 */
	public GroupChatDTO selectGroupChatByMessageNo(long messageNo) {
		return mapper.selectGroupChatByMessageNo(messageNo);
	}
	
	/** Map filter 를 기준으로 메세지 검색 */
	public List<GroupChatDTO> selectMessagesByFilters(MessageSearchFilterDTO dto) {
		return mapper.selectMessagesByFilters(dto);
	}
	
	/** groupNo, messageNo 로 messageNo 기준 -10 +10 메세지 조회 */
	public List<GroupChatDTO> selectMessagesAround(Map<String, Object> param) {
		return mapper.selectMessagesAround(param);
	}
	
	/** group_member 테이블의 닉네임 조회 */
	public String selectGroupNickname(int groupNo, int userNo) {
		return mapper.selectGroupNickname(groupNo, userNo);
	}
	
}