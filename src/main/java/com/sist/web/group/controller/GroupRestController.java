package com.sist.web.group.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.code.GroupErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.common.exception.domain.GroupException;
import com.sist.web.common.response.ApiResponse;
import com.sist.web.group.dto.GroupDTO;
import com.sist.web.group.dto.GroupJoinRequestsDTO;
import com.sist.web.group.service.GroupService;
import com.sist.web.group.dto.GroupMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupRestController {
	private final GroupService service;
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> group_groups(HttpServletRequest request)
	{
		Map<String, Object> map = new HashMap<>();
		try {
			Long userNo = (Long)request.getAttribute("userno");
			int user_no = userNo.intValue();
			System.out.println(user_no);
			map = service.getGroupListAndStates(user_no);
			System.out.println(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("======== error ========");
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		System.out.println("group_vue 완료");
		return ResponseEntity.ok(ApiResponse.success(map));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<GroupDTO>> createGroup(@Valid @RequestPart("group") GroupDTO dto, @RequestPart(value = "image", required = false) MultipartFile image) {
		// 피드 이미지와 통일시켜야 할 수도 있음
		try {
			if (image != null && !image.isEmpty()) {
				String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
				File file = new File(uploadDir, filename);
				image.transferTo(file);
				
				dto.setProfile_img("/images/group/" + filename);
			}
			service.createGroup(dto);
			return ResponseEntity.ok(ApiResponse.success(dto));
		} catch (IOException ex) {
			throw new GroupException(GroupErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}
	
	@GetMapping("/{users}")
	public ResponseEntity<ApiResponse<List<GroupDTO>>> getGroupAll(HttpServletRequest request, @PathVariable Integer users) {
		Long userNo = (Long)request.getAttribute("userno");
		if (userNo == null) {
			throw new GroupException(GroupErrorCode.USER_NOT_FOUND);
		}
		List<GroupDTO> list = service.getGroupAll(String.valueOf(userNo));
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<GroupMemberDTO>>> getGroupMemberAllByGroupNo(Integer groupNo) {
		if (groupNo == null) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		List<GroupMemberDTO> list = service.getGroupMemberAllByGroupNo(groupNo);
		return ResponseEntity.ok(ApiResponse.success(list));
	}
	
	@PostMapping("/members")
	public ResponseEntity<ApiResponse<GroupMemberDTO>> addGroupMember(@Valid @RequestBody GroupMemberDTO dto) {
		service.addGroupMember(dto);
		return ResponseEntity.ok(ApiResponse.success(dto, "그룹 멤버 추가 성공"));
	}
	
	@PostMapping("/{groupNo}/join")
	public ResponseEntity<ApiResponse<Map<String, Object>>> joinGroupRequests(HttpServletRequest request, int group_no)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		GroupJoinRequestsDTO dto = new GroupJoinRequestsDTO();
		try {
			Long userNo = (Long)request.getAttribute("userno");
			dto.setUser_no(userNo);
			dto.setGroup_no(group_no);
			service.insertJoinRequests(dto);
			map.put("userNo", userNo);
			map.put("groupNo", group_no);
			
		} catch (Exception e) {
			log.info("가입 신청 실패: {}", e.getMessage());
			throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(ApiResponse.success(map));
	}
	
	@GetMapping("/{groupNo}/detail")
	public ResponseEntity<ApiResponse<GroupDTO>> getGroupDetailByGroupNo(@PathVariable Integer groupNo) {
		if (groupNo == null) {
			throw new GroupException(GroupErrorCode.GROUP_NOT_FOUND);
		}
		return ResponseEntity.ok(ApiResponse.success(service.getGroupDetailByGroupNo(groupNo)));
	}
	
	@PutMapping(value = "/{groupNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateGroupDetail(
        @PathVariable("groupNo") Integer groupNo,
        @RequestPart("group_name") String groupName,
        @RequestPart("description") String description,
        @RequestPart("capacity") Integer capacity,
        @RequestPart("is_public") String isPublic,
        @RequestPart(value = "profile_img", required = false) MultipartFile profileImg) {

        String imagePath = null;

        if (profileImg != null && !profileImg.isEmpty()) {
            try {
                String filename = UUID.randomUUID() + "_" + profileImg.getOriginalFilename();
                File target = new File(uploadDir, filename);
                profileImg.transferTo(target);
                imagePath = filename; 
            } catch (Exception e) {
            	
            }
        }

        GroupDTO dto = new GroupDTO();
        dto.setGroup_no(groupNo);
        dto.setGroup_name(groupName);
        dto.setDescription(description);
        dto.setCapacity(capacity);
        dto.setIs_public(isPublic);
        dto.setProfile_img(imagePath); // null이면 쿼리에서 제외됨

        service.updateGroupDetail(dto);
        return ResponseEntity.ok(ApiResponse.success("그룹 정보가 수정되었습니다."));
    }
	
}
