package com.sist.web.group.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	@Value("${file.upload-dir}")
	private String uploadDir;
	private static final String ROLE_OWNER = "OWNER";
	private static final String GROUP_IMAGE_PATH_PREFIX = "/images/group/";

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
	public void createGroup(GroupDTO dto, MultipartFile profileImg) {
		if (profileImg != null && !profileImg.isEmpty()) {
			try {
				File uploadPath = new File(uploadDir);
	            if (!uploadPath.exists()) {
	                uploadPath.mkdirs(); // 디렉토리 없으면 생성
	            }
				
	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
	            String extension = "";
				
				
	            String originalFilename = profileImg.getOriginalFilename();
	            int lastDot = originalFilename.lastIndexOf(".");
	            if (lastDot != -1) {
	                extension = originalFilename.substring(lastDot); // 예: .jpg
	            }
				
	            String filename = timestamp + "_" + (int)(Math.random() * 1000) + extension;
	            
	            File file = new File(uploadPath, filename);
	            profileImg.transferTo(file);
	            
	            
				profileImg.transferTo(file);
				
				dto.setProfile_img(GROUP_IMAGE_PATH_PREFIX + filename);
			} catch (Exception ex) {
				throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
			}
		}
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
	public void updateGroupDetail(GroupDTO dto, MultipartFile profileImg) {
		System.out.println("service dto: " + dto.toString());
		if (profileImg != null && !profileImg.isEmpty()) {
			try {
				
				File uploadPath = new File(uploadDir);
	            if (!uploadPath.exists()) {
	                uploadPath.mkdirs(); 
	            }

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
	            String extension = "";

	            String originalFilename = profileImg.getOriginalFilename();
	            int lastDot = originalFilename.lastIndexOf(".");
	            if (lastDot != -1) {
	                extension = originalFilename.substring(lastDot); // 예: .jpg
	            }

	            String filename = timestamp + "_" + (int)(Math.random() * 1000) + extension;

	            File file = new File(uploadPath, filename);
	            profileImg.transferTo(file);

				dto.setProfile_img(GROUP_IMAGE_PATH_PREFIX + filename);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
			}
		}
		gDao.updateGroupDetail(dto);
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


}
