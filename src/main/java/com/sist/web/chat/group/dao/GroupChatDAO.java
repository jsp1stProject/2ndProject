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
	
	/** 사용자가 전송한 메세지 DB 삽입 */
	public void insertGroupChatMessage(GroupChatVO vo) {
		mapper.insertGroupChatMessage(vo);
	}
	/** 그룹 번호 기준으로 해당 그룹의 최근 메시지 20개 조회 */
	public List<GroupChatVO> selectLatestMessageByGroupNo(int groupNo, Long lastMessageId) {
		return mapper.selectLatestMessageByGroupNo(groupNo, lastMessageId);
	}
	/** 그룹 생성 (필요 데이터: group_name, description, owner - user_no) */
	public void insertGroup(GroupVO vo) {
		mapper.insertGroup(vo);
	}
	/** 그룹원 추가 (필요 데이터: group_no, user_no) */
	public void insertGroupMember(GroupMemberVO vo) {
		mapper.insertGroupMember(vo);
	}
	/** 해당 user_no 가 속한 모든 그룹 조회 */
	public List<GroupVO> selectGroup(String userNo) {
		return mapper.selectGroup(userNo);
	}
	/** message_no 를 기준으로 해당 메세지의 모든 정보 조회 */
	public GroupChatVO selectGroupChatByMessageNo(long messageNo) {
		return mapper.selectGroupChatByMessageNo(messageNo);
	}
}