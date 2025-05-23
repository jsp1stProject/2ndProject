package com.sist.web.admin.service;

import com.sist.web.admin.dto.AdminSitterAppDetailDTO;
import com.sist.web.mypage.vo.PetDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminService {
    public Map<String, Object> pagingMap(String page, Map query);

    public Map getPageBlock(int curpage, int PAGEBLOCK, int totalPage);

    public Map getAllUsers(String page, Map query);

    public Map getAllSitters(String page, Map query);

    public Map getSitterApps(String page, Map query);

    public Map getUserDetail(String user_no);

    public AdminSitterAppDetailDTO getSitterAppDetail(String user_no);

    public List<PetDTO> getPetsDetail(String user_no);

}
