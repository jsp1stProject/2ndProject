package com.sist.web.group.service;

import java.util.List;
import java.util.Map;

import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.dto.GroupMemberDTO;

public interface GroupService {
	public List<GroupDTO> getGroupAllList();
	public List<Map<String, Object>> selectGroupMemberStates(int user_no);
	public Map<String, Object> getGroupListAndStates(int user_no);
	public GroupDTO getGroupDetailByGroupNo(int group_no);
	void createGroup(GroupDTO vo);
	void addGroupMember(GroupMemberDTO vo);
	List<GroupDTO> getGroupAll(String userId);
	List<GroupMemberDTO> getGroupMemberAllByGroupNo(int groupNo);
	public void insertJoinRequests(GroupJoinRequestsDTO dto);
<<<<<<< HEAD
	public void updateGroupDetail(GroupDTO dto);
=======
	public List<GroupJoinRequestsDTO> selectGroupRequestsData(int user_no);
	public void updateJoinRequestStatus(GroupJoinRequestsDTO dto);
	public void joinRequestResult(int request_no, int group_no, long user_no, String status, String nickname);
>>>>>>> feature/schedule
}
