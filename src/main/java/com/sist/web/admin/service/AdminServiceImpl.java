package com.sist.web.admin.service;

import com.sist.web.admin.dto.AdminSitterAppDetailDTO;
import com.sist.web.admin.dto.AdminSitterListDTO;
import com.sist.web.admin.mapper.AdminMapper;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.admin.dto.AdminUserDetailDTO;
import com.sist.web.user.vo.UserDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final int ROWSIZE = 12;
    private final int PAGEBLOCK = 10;

    @Override
    public Map<String, Object> pagingMap(String page, Map query) {
        if(page==null || page.equals("")) page="1";
        int curpage=Integer.parseInt(page);
        Map<String, Object> map=new HashMap<>();
        map.put("curpage",curpage);
        map.put("start",(curpage-1)*ROWSIZE);
        map.put("rowsize",ROWSIZE);
        map.put("f_rowsize",(float)ROWSIZE);
        if(query.get("mail")!=null){
            map.put("user_mail",query.get("mail"));
        }
        return map;
    }

    @Override
    public Map getPageBlock(int curpage, int PAGEBLOCK, int totalPage) {
        int startPage=(curpage/PAGEBLOCK)*PAGEBLOCK+1;
        int endPage=((curpage/PAGEBLOCK)+1)*PAGEBLOCK;
        if (endPage>totalPage) endPage=totalPage;
        Map result=new HashMap();
        result.put("startPage",startPage);
        result.put("endPage",endPage);
        return result;
    }

    @Override
    public Map getAllUsers(String page, Map query) {
        Map<String, Object> map=pagingMap(page,query);

        List<AdminUserDetailDTO> list=adminMapper.getUsers(map);
        int totalPage=adminMapper.getUsersTotalPage(map);
        Map<String, Integer> pages = getPageBlock((Integer) map.get("curpage"), PAGEBLOCK, totalPage);

        Map result=new HashMap();
        result.put("totalPage",totalPage);
        result.put("startPage",pages.get("startPage"));
        result.put("endPage",pages.get("endPage"));
        result.put("list",list);

        return result;
    }

    @Override
    public Map getAllSitters(String page, Map query) {
        Map<String, Object> map=pagingMap(page,query);
        map.put("authority","ROLE_SITTER");

        List<AdminUserDetailDTO> list=adminMapper.getUsers(map);
        int totalPage=adminMapper.getUsersTotalPage(map);
        Map<String, Integer> pages = getPageBlock((Integer) map.get("curpage"), PAGEBLOCK, totalPage);

        Map result=new HashMap();
        result.put("totalPage",totalPage);
        result.put("startPage",pages.get("startPage"));
        result.put("endPage",pages.get("endPage"));
        result.put("list",list);

        return result;
    }

    @Override
    public Map getSitterApps(String page, Map query) {
        Map<String, Object> map=pagingMap(page,query);

        List<AdminSitterListDTO> list=adminMapper.getSitterApps(map);
        int totalPage=adminMapper.getSitterAppsTotalPage(map);
        Map<String, Integer> pages = getPageBlock((Integer) map.get("curpage"), PAGEBLOCK, totalPage);

        Map result=new HashMap();
        result.put("totalPage",totalPage);
        result.put("startPage",pages.get("startPage"));
        result.put("endPage",pages.get("endPage"));
        result.put("list",list);

        return result;
    }

    @Override
    public Map getUserDetail(String user_no) {
        if(user_no==null || user_no.equals("")){
            throw new CommonException(CommonErrorCode.NOT_FOUND);
        }
        Map map=new HashMap();
        map.put("user",adminMapper.getUserDetail(user_no));
        map.put("group",adminMapper.getUserGroupList(user_no));
        return map;
    }

    @Override
    public AdminSitterAppDetailDTO getSitterAppDetail(String user_no) {
        return adminMapper.getSitterAppDetail(user_no);
    }

    @Override
    public List<PetDTO> getPetsDetail(String user_no) {
        if(user_no==null || user_no.equals("")){
            throw new CommonException(CommonErrorCode.NOT_FOUND);
        }
        return adminMapper.getPetsDetail(user_no);
    }

}
