package com.sist.web.group.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.group.dao.GroupDAO;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.groupchat.dto.GroupMemberDTO;
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
	
	@Transactional
	@Override
	public void createGroup(GroupDTO vo) {
		gDao.insertGroup(vo);
		
		GroupMemberDTO member = new GroupMemberDTO();
		// group_no, user_no, nickname
		member.setGroup_no(vo.getGroup_no());
		member.setUser_no(vo.getOwner());
		member.setRole("OWNER");
		
		gDao.insertGroupMember(member);
	}
	
	@Override
	public void addGroupMember(GroupMemberDTO vo) {
		gDao.insertGroupMember(vo);
	}
	
	@Override
	public List<GroupDTO> getGroupAll(String userNo) {
		if (userNo == null) {
	        throw new CommonException(CommonErrorCode.MISSING_PARAMETER);
	    }
	    return gDao.selectGroup(userNo);
	}
	
	@Override
	public List<GroupMemberDTO> getGroupMemberAllByGroupNo(int groupNo) {
		List<GroupMemberDTO> list = new ArrayList<GroupMemberDTO>();
		try {
			list = gDao.selectGroupMemberAllByGroupNo(groupNo);
		} catch (Exception ex) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		return list;
	}
}
