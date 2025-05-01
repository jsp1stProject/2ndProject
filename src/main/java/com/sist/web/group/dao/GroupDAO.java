package com.sist.web.group.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.mapper.GroupMapper;

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
}
