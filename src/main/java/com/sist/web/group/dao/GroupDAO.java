package com.sist.web.group.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.mapper.GroupMapper;
import com.sist.web.group.dto.GroupMemberDTO;
import com.sist.web.group.dto.GroupMemberInfoDTO;

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
	
	/** 그룹 삭제 */
	public void deleteGroup(int groupNo) {
		mapper.deleteGroup(groupNo);
	}
	
	/** 그룹의 현재 인원 조회 */
	public int selectMemberCountByGroupNo(int groupNo) {
		return mapper.selectMemberCountByGroupNo(groupNo);
	}
	
	/** 그룹 멤버 상세정보 조회 */
	public GroupMemberInfoDTO selectGroupMemberInfo(int groupNo, int userNo) {
		return mapper.selectGroupMemberInfo(groupNo, userNo);
	}
	
	/** 채팅방 입장시 viewing 열람 중 컬럼 1 update */
	public void updateViewingStatus(int groupNo, int userNo, int viewing) {
		mapper.updateViewingStatus(groupNo, userNo, viewing);
	}
	
	/** 채팅방 나갈 시 viewing 열람 중 컬럼 0 update, lastReadMessageNo 최신 메세지로 update */
	public void updateExitStatus(int groupNo, int userNo, Long lastReadMessagesNo) {
		mapper.updateExitStatus(groupNo, userNo, lastReadMessagesNo);
	}
	
	/** 채팅방 연결 끊김 추적 */
	public void updateLastSeenAt(int userNo) {
		mapper.updateLastSeenAt(userNo);
	}
	
	/** 채팅방 연결 끊김 추적 후 연길 끊김 확인 시 viewing 0 update */
	public void markInactiveUsers() {
		mapper.markInactiveUsers();
	}
	
	/** markInactiveUsers 보완 메서드(추적 실패 시) */
	public void updateViewingZero(int userNo) {
		mapper.updateViewingZero(userNo);
	}
	
	/** 그룹 태그 조회 */
	public List<String> selectGroupTagsByGroupNo(int groupNo) {
		return mapper.selectGroupTagsByGroupNo(groupNo);
	}
	
	/** 그룹별 닉네임 변경 */
	public void updateGroupMemberNickname(int userNo, String nickname, int groupNo) {
		mapper.updateGroupMemberNickname(userNo, nickname, groupNo);
	}
	
	public void deleteGroupMembers(int groupNo) {
		mapper.deleteGroupMembers(groupNo);
	}
	
	public void deleteJoinRequests(int groupNo) {
		mapper.deleteJoinRequests(groupNo);
	}
	
	public void deleteGroupMessage(int groupNo) {
		mapper.deleteGroupMessage(groupNo);
	}
	
	public void deleteGroupTagAll(int groupNo) {
		mapper.deleteGroupTagAll(groupNo);
	}
	/*
	public void deleteGroupFeedAll(int groupNo) {
		mapper.deleteGroupFeedAll(groupNo);
	}
	public void deleteGroupFeedCommentAll(int groupNo) {
		mapper.deleteGroupFeedCommentAll(groupNo);
	}
	public void deleteGroupFeedFileInfoAll(int groupNo) {
		mapper.deleteGroupFeedFileInfoAll(groupNo);
	}
	public void deleteGroupFeedLikeAll(int groupNo) {
		mapper.deleteGroupFeedLikeAll(groupNo);
	}
	*/
	public Integer selectGroupFeed(int groupNo) {
		return mapper.selectGroupFeedNo(groupNo);
	}
}
