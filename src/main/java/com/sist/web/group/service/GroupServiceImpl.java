package com.sist.web.group.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.group.dao.GroupDAO;
import com.sist.web.group.dto.GroupDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
	private final GroupDAO gDao;

	@Override
	public List<GroupDTO> getGroupAllList() {
		return gDao.selectGroupAllList();
	}

	@Override
	public GroupDTO getGroupDetail(int group_no) {
		return gDao.selectGroupDetail(group_no);
	}
	
	
}
