package com.sist.web.admin.service;

import com.sist.web.admin.mapper.AdminMapper;
import com.sist.web.mypage.vo.SitterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;

    public List<SitterDTO> getAllSitters(String page) {
        if(page==null || page.equals("")){
            page="1";
        }
        int curpage=Integer.parseInt(page);
        int start = (10*curpage)-9;
        int end = 10*curpage;

        return adminMapper.getAllSitters(start, end);
    }
}
