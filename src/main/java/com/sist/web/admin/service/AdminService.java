package com.sist.web.admin.service;

import co.elastic.clients.elasticsearch.ml.Page;
import com.sist.web.admin.mapper.AdminMapper;
import com.sist.web.mypage.vo.SitterDTO;
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
public class AdminService {
    private final AdminMapper adminMapper;

    public Map getAllSitters(String page, Map query) {
        if(page==null || page.equals("")){
            page="1";
        }
        int curpage=Integer.parseInt(page);
        int ROWSIZE=12;
        int PAGEBLOCK=10;
        Map<String, Object> map=new HashMap<>();
        map.put("authority","ROLE_SITTER");
        map.put("start",(curpage-1)*ROWSIZE);
        map.put("rowsize",ROWSIZE);
        map.put("f_rowsize",(float)ROWSIZE);
        if(query.get("mail")!=null){map.put("user_mail",query.get("mail"));}

        List<UserDetailDTO> list=adminMapper.getUsers(map);
        int totalPage=adminMapper.getUsersTotalPage(map);
        Map<String, Integer> pages = getPageBlock(curpage, PAGEBLOCK, totalPage);

        Map result=new HashMap();
        result.put("totalPage",totalPage);
        result.put("startPage",pages.get("startPage"));
        result.put("endPage",pages.get("endPage"));
        result.put("list",list);

        return result;
    }

    public Map getSitterApps(String page, Map query) {
        if(page==null || page.equals("")){
            page="1";
        }
        int curpage=Integer.parseInt(page);
        int ROWSIZE=12;
        int PAGEBLOCK=10;
        Map<String, Object> map=new HashMap<>();
        map.put("start",(curpage-1)*ROWSIZE);
        map.put("rowsize",ROWSIZE);
        map.put("f_rowsize",(float)ROWSIZE);
        if(query.get("mail")!=null){map.put("user_mail",query.get("mail"));}

        List<SitterDTO> list=adminMapper.getSitterApps(map);
        int totalPage=adminMapper.getSitterAppsTotalPage(map);
        Map<String, Integer> pages = getPageBlock(curpage, PAGEBLOCK, totalPage);

        Map result=new HashMap();
        result.put("totalPage",totalPage);
        result.put("startPage",pages.get("startPage"));
        result.put("endPage",pages.get("endPage"));
        result.put("list",list);

        return result;
    }

    public Map getPageBlock(int curpage, int PAGEBLOCK, int totalPage) {
        int startPage=(curpage/PAGEBLOCK)*PAGEBLOCK+1;
        int endPage=((curpage/PAGEBLOCK)+1)*PAGEBLOCK;
        if (endPage>totalPage) endPage=totalPage;
        Map result=new HashMap();
        result.put("startPage",startPage);
        result.put("endPage",endPage);
        return result;
    }
}
