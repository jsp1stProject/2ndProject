package com.sist.web.group.service;

import java.util.List;

import com.sist.web.group.dto.GroupDTO;

public interface GroupService {
	public List<GroupDTO> getGroupAllList();
	public GroupDTO getGroupDetail(int group_no);
}
