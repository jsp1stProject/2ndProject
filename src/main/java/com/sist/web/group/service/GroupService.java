package com.sist.web.group.service;

import java.util.List;

import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupMemberDTO;

public interface GroupService {
	public List<GroupDTO> getGroupAllList();
	public GroupDTO getGroupDetail(int group_no);
	void createGroup(GroupDTO vo);
	void addGroupMember(GroupMemberDTO vo);
	List<GroupDTO> getGroupAll(String userId);
	List<GroupMemberDTO> getGroupMemberAllByGroupNo(int groupNo);
}
