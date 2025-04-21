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
	
	/** 메세지 삽입 */
	public void insertGroupChatMessage(GroupChatVO vo) {
		mapper.insertGroupChatMessage(vo);
	}
	
	/** 최근 메세지 20개 조회 */
	public List<GroupChatVO> selectLatestMessageByGroupId(long groupId, long lastMessageId) {
		return mapper.selectLatestMessageByGroupId(groupId, lastMessageId);
	}
	
	/** 그룹 생성 */
	public void insertGroup(GroupVO vo) {
		mapper.insertGroup(vo);
	}
	
	/** 그룹원 추가 */
	public void insertGroupMember(GroupMemberVO vo) {
		mapper.insertGroupMember(vo);
	}
	
	/** 그룹 조회 */
	public List<GroupVO> selectGroup(String userId) {
		return mapper.selectGroup(userId);
	}
}