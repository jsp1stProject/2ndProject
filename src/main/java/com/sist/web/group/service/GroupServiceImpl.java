package com.sist.web.group.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.group.dao.GroupDAO;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.dto.GroupMemberDTO;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
	private final GroupDAO gDao;
	private final UserMapper userMapper;

	@Override
	public List<GroupDTO> getGroupAllList() {
		return gDao.selectGroupAllList();
	}

	@Override
	public List<Map<String, Object>> selectGroupMemberStates(int user_no) {
		// TODO Auto-generated method stub
		return gDao.selectGroupMemberStates(user_no);
	}
	
	@Override
	public Map<String, Object> getGroupListAndStates(int user_no){
		
		List<GroupDTO> group_list = gDao.selectGroupAllList();
		List<Map<String, Object>> states_list = gDao.selectGroupMemberStates(user_no);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group_list", group_list);
		map.put("states_list", states_list);
		map.put("user_no", user_no);
		return map;
	}
	@Override
	public GroupDTO getGroupDetailByGroupNo(int group_no) {
		GroupDTO dto = new GroupDTO();
		try {
			dto = gDao.selectGroupDetail(group_no);
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return dto;
	}
	
	@Transactional
	@Override
	public void createGroup(GroupDTO dto) {
		gDao.insertGroup(dto);
		
		GroupMemberDTO member = new GroupMemberDTO();
		UserVO user = userMapper.getUserMailFromUserNo(String.valueOf(dto.getOwner()));
		if (user == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		// group_no, user_no, nickname
		member.setGroup_no(dto.getGroup_no());
		member.setUser_no(dto.getOwner());
		member.setRole("OWNER");
		member.setNickname(user.getNickname());
		
		gDao.insertGroupMember(member);
	}
	
	@Override
	public void addGroupMember(GroupMemberDTO dto) {
		UserVO user = userMapper.getUserMailFromUserNo(String.valueOf(dto.getUser_no()));
		if (user == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		dto.setNickname(user.getNickname());
		gDao.insertGroupMember(dto);
	}
	
	@Override
	public List<GroupDTO> getGroupAll(String userNo) {
		List<GroupDTO> list = new ArrayList<GroupDTO>();
		try {
			list = gDao.selectGroup(userNo);
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	    return list;
	}
	
	@Override
	public List<GroupMemberDTO> getGroupMemberAllByGroupNo(int groupNo) {
		List<GroupMemberDTO> list = new ArrayList<GroupMemberDTO>();
		try {
			list = gDao.selectGroupMemberAllByGroupNo(groupNo);
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	@Override
	public void insertJoinRequests(GroupJoinRequestsDTO dto) {
		gDao.insertJoinRequests(dto);
		
	}

	@Override
	public void updateGroupDetail(GroupDTO dto) {
		gDao.updateGroupDetail(dto);
	}
}
