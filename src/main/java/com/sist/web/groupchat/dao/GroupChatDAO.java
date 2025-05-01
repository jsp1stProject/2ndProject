package com.sist.web.groupchat.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.sist.web.groupchat.dto.GroupChatDTO;
import com.sist.web.groupchat.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;
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
	/** 그룹 생성 (필요 데이터: group_name, description, owner - user_no) */
	
	public void insertGroup(GroupDTO vo) {
		mapper.insertGroup(vo);
	}
	
	/** 그룹원 추가 (필요 데이터: group_no, user_no) */
	public void insertGroupMember(GroupMemberDTO vo) {
		mapper.insertGroupMember(vo);
	}
	/** 해당 user_no 가 속한 모든 그룹 조회 */
	public List<GroupDTO> selectGroup(String userNo) {
		return mapper.selectGroup(userNo);
	}
	/** message_no 를 기준으로 해당 메세지의 모든 정보 조회 */
	public GroupChatDTO selectGroupChatByMessageNo(long messageNo) {
		return mapper.selectGroupChatByMessageNo(messageNo);
	}
	/** 그룹 번호 기준으로 해당 그룹의 모든 멤버 조회 */
	public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo) {
		return mapper.selectGroupMemberAllByGroupNo(groupNo);
	}
}