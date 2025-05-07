package com.sist.web.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.mapper.GroupMapper;
import com.sist.web.groupchat.dto.GroupMemberDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupDAO {
	private final GroupMapper mapper;
	
	public List<GroupDTO> selectGroupAllList()
	{
		return mapper.selectGroupAllList();
	}
	
	public GroupDTO selectGroupDetail(int group_no)
	{
		return mapper.selectGroupDetail(group_no);
	}
	
	public int groupInsertData(GroupDTO dto)
	{
		mapper.groupInsertData(dto);
		return mapper.groupCurrentNodata();
	}
	
	/** 그룹 생성 (필요 데이터: group_name, description, owner - user_no) */
	public void insertGroup(GroupDTO dto) {
		mapper.insertGroup(dto);
	}
	
	/** 그룹원 추가 (필요 데이터: group_no, user_no) */
	public void insertGroupMember(GroupMemberDTO vo) {
		mapper.insertGroupMember(vo);
	}
	
	/** 해당 user_no 가 속한 모든 그룹 조회 */
	public List<GroupDTO> selectGroup(String userNo) {
		return mapper.selectGroup(userNo);
	}
	
	/** 그룹 번호 기준으로 해당 그룹의 모든 멤버 조회 */
	public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo) {
		return mapper.selectGroupMemberAllByGroupNo(groupNo);
	}
}
