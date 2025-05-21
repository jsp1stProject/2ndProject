package com.sist.web.admin.service;

import co.elastic.clients.elasticsearch.ml.Page;
import com.sist.web.admin.mapper.AdminMapper;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.user.vo.UserDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public interface AdminService {
    public Map<String, Object> pagingMap(String page, Map query);

    public Map getPageBlock(int curpage, int PAGEBLOCK, int totalPage);

    public Map getAllUsers(String page, Map query);

    public Map getAllSitters(String page, Map query);

    public Map getSitterApps(String page, Map query);

    public SitterDTO getSitterAppDetail(String user_no);

    public List<PetDTO> getPetsDetail(String user_no);

}
