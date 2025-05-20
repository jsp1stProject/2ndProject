package com.sist.web.group.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.mapper.GroupMapper;
import com.sist.web.group.dto.GroupMemberDTO;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupDAO {
	private final GroupMapper mapper;
	
	/** 그룹 전체 리스트 */
	public List<GroupDTO> selectGroupAllList()
	{
		return mapper.selectGroupAllList();
	}
	/** 로그인한 유저의 그룹가입 상태리스트 */
	public List<Map<String, Object>> selectGroupMemberStates(int user_no)
	{
		return mapper.selectGroupMemberStates(user_no);
	}
	
	public GroupDTO selectGroupDetail(int group_no)
	{
		return mapper.selectGroupDetail(group_no);
	}
	
	/** 그룹 상세데이터(그룹장닉네임, 현재참여인원 포함) */
	public GroupDTO selectGroupDetailTotal(int group_no)
	{
		return mapper.selectGroupDetailTotal(group_no);
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
	
	/** 그룹가입 신청 (필요 데이터: group_no, user_no) */
	public void insertJoinRequests(GroupJoinRequestsDTO dto) {
		mapper.insertJoinRequests(dto);
	}
	
	/** 해당 user_no 가 속한 모든 그룹 조회 */
	public List<GroupDTO> selectGroup(String userNo) {
		return mapper.selectGroup(userNo);
	}
	
	/** 그룹 번호 기준으로 해당 그룹의 모든 멤버 조회 */
	public List<GroupMemberDTO> selectGroupMemberAllByGroupNo(@Param("group_no") int groupNo) {
		return mapper.selectGroupMemberAllByGroupNo(groupNo);
	}
	
	/** 그룹 상세 수정 */
	public void updateGroupDetail(GroupDTO dto) {
		mapper.updateGroupDetail(dto);
	}
	/** 생성한 모든 그룹에 대한 가입요청리스트 조회 */
	public List<GroupJoinRequestsDTO> selectGroupRequestsData(int user_no)
	{
		return mapper.selectGroupRequestsData(user_no);
	}
	
	/** 가입요청에 대한 상태 수정 */
	public void updateJoinRequestStatus(GroupJoinRequestsDTO dto) {
		mapper.updateJoinRequestStatus(dto);
	}
	
	/** 그룹 태그 삽입 */
	public void insertGroupTags(Map<String, Object> map) {
		mapper.insertGroupTags(map);
	}
	
	/** 그룹 태그 수정 전 태그 모두 삭제 */
	public void deleteGroupTags(int groupNo) {
		mapper.deleteGroupTags(groupNo);
	}
}
