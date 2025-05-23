package com.sist.web.group.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.aws.AwsS3Service;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.group.dao.GroupDAO;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.dto.GroupMemberDTO;
import com.sist.web.group.dto.GroupMemberInfoDTO;
import com.sist.web.groupchat.dao.GroupChatDAO;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
	private final GroupDAO gDao;
	private final GroupChatDAO cDao;
	private final AwsS3Service awsS3;
	private final UserMapper userMapper;
	
	
	@Value("${aws.url}")
	private String s3BaseUrl;
	
	private static final String FILE_DIR = "group/";
	private static final String THUMBNAIL_DIR = "group/thumbnail";
	private static final int THUMBNAIL_WIDTH = 100; 
	private static final int THUMBNAIL_HEIGHT = 100; 
	private static final String ROLE_OWNER = "OWNER"; 
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
		for(GroupDTO group : group_list)
		{
			int group_no = group.getGroup_no();
			group.setTags(gDao.selectGroupTagsByGroupNo(group_no));
		}
		List<Map<String, Object>> states_list = gDao.selectGroupMemberStates(user_no);
		List<GroupDTO> joinedgroup_list = gDao.selectGroup(String.valueOf(user_no));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("group_list", group_list);
		map.put("states_list", states_list);
		map.put("user_no", user_no);
		map.put("joinedgroup_list", joinedgroup_list);
		return map;
	}
	@Transactional
	@Override
	public GroupDTO getGroupDetailByGroupNo(int group_no) {
		GroupDTO dto = new GroupDTO();
		try {
			dto = gDao.selectGroupDetail(group_no);
			dto.setTags(gDao.selectGroupTagsByGroupNo(group_no));
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return dto;
	}
	
	@Override
	public GroupDTO getGroupDetailTotal(int group_no) {
		// TODO Auto-generated method stub
		GroupDTO dto = new GroupDTO();
		try {
			dto = gDao.selectGroupDetailTotal(group_no);
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return dto;
	}
	
	@Transactional
	@Override
	public void createGroup(GroupDTO dto, MultipartFile profileImg) {
		if (profileImg != null && !profileImg.isEmpty()) {
			dto.setProfile_img(uploadThumbnailImage(profileImg));
		}
		System.out.println("##### dto:"+dto);
		gDao.insertGroup(dto);
		
		GroupMemberDTO member = new GroupMemberDTO();
		UserVO user = userMapper.getUserMailFromUserNo(String.valueOf(dto.getOwner()));
		if (user == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		// group_no, user_no, nickname
		member.setGroup_no(dto.getGroup_no());
		member.setUser_no(dto.getOwner());
		member.setRole(ROLE_OWNER);
		List<String> tags=dto.getTags();
		member.setNickname(user.getNickname());
		if (tags != null && !tags.isEmpty()) {
			for (String tag : tags) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("group_no", dto.getGroup_no());
				param.put("tag", tag);
				
				gDao.insertGroupTags(param);
			}
		}
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
		List<GroupMemberDTO> list;
		try {
			list = gDao.selectGroupMemberAllByGroupNo(groupNo);
			
			for (GroupMemberDTO dto : list) {
				if (dto.getProfile() != null && !dto.getProfile().isBlank()) {
					dto.setProfile(s3BaseUrl + dto.getProfile());
				}
			}
		} catch (Exception ex) {
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return list;
	}
	
	
	@Override
	public void insertJoinRequests(GroupJoinRequestsDTO dto) {
		gDao.insertJoinRequests(dto);
		
	}

	@Transactional
	@Override
	public void updateGroupDetail(GroupDTO dto, MultipartFile profileImg, List<String> tags) {
		
		if (profileImg != null && !profileImg.isEmpty()) {
			dto.setProfile_img(uploadThumbnailImage(profileImg));
		}
		
		
		gDao.updateGroupDetail(dto);
		
		gDao.deleteGroupTags(dto.getGroup_no());
		
		if (tags != null && !tags.isEmpty()) {
			for (String tag : tags) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("group_no", dto.getGroup_no());
				param.put("tag", tag);
				
				gDao.insertGroupTags(param);
			}
		}
		
	}
	public List<GroupJoinRequestsDTO> selectGroupRequestsData(int user_no) {
		return gDao.selectGroupRequestsData(user_no);
	}
	
	@Override
	public void updateJoinRequestStatus(GroupJoinRequestsDTO dto) {
		gDao.updateJoinRequestStatus(dto);
	}
	
	@Transactional
	@Override
	public void joinRequestResult(int request_no, int group_no, long user_no,String status, String nickname) {
		System.out.println("서비스임플진입");
		GroupJoinRequestsDTO dto = new GroupJoinRequestsDTO();
		dto.setRequest_no(request_no);
		dto.setStatus(status);
		gDao.updateJoinRequestStatus(dto);
		System.out.println("상태업데이트");
		if("APPROVED".equals(status))
		{
			//그룹멤버에 추가하기
			GroupMemberDTO gmdto = new GroupMemberDTO();
			gmdto.setGroup_no(group_no);
			gmdto.setUser_no((int)user_no);
			gmdto.setNickname(nickname);
			gDao.insertGroupMember(gmdto);
			System.out.println("멤버추가하기");
			//닉네임을 받으려면 mapper를 추가해야할까
			//내가 가지고 있는 데이터 -> group_no, user_no(수정해야할), status, 닉네임을 같이 넘겨받아야해 
			
		}
		
		
	}
	@Transactional
	@Override
	public void removeGroup(int groupNo, int userNo) {
		GroupDTO dto = gDao.selectGroupDetail(groupNo);
		int memberCount = gDao.selectMemberCountByGroupNo(groupNo);
		if (memberCount != 1) {
			throw new GroupException(GroupErrorCode.CANNOT_DELETE_GROUP_WITH_MULTIPLE_MEMBERS);
		} else if (dto.getOwner() != userNo) {
			throw new GroupException(GroupErrorCode.NOT_GROUP_OWNER);
		}
		// 해당 group_no 의 모든 태그, 가입 신청, 멤버, 메세지, 그룹 삭제
		gDao.deleteGroupTagAll(groupNo);
		gDao.deleteJoinRequests(groupNo);
		gDao.deleteGroupMembers(groupNo);
		gDao.deleteGroupMessage(groupNo);
		gDao.deleteGroup(groupNo);
	}
	
	@Override
	public GroupMemberInfoDTO getGroupMemberDetail(int groupNo, int userNo) {
		// 기타 로직 수행 필요
		GroupMemberInfoDTO dto = gDao.selectGroupMemberInfo(groupNo, userNo);
		dto.setProfile(s3BaseUrl + dto.getProfile());
		return dto;
	}
	
	private String uploadThumbnailImage(MultipartFile file) {
		try {
			String key = awsS3.ResizeAndUploadFile(file, THUMBNAIL_DIR, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
			return s3BaseUrl + key;
		} catch (Exception ex) {
			log.error("썸네일 이미지 업로드 실패", ex);
			throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}
	
	@Override
	public void updateGroupMemberNickname(int userNo, String nickname, int groupNo) {
		gDao.updateGroupMemberNickname(userNo, nickname, groupNo);
	}

	
}
