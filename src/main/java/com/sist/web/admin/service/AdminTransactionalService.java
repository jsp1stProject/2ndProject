package com.sist.web.admin.service;

import com.sist.web.admin.dto.AdminSitterAppDetailDTO;
import com.sist.web.admin.mapper.AdminMapper;
import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminTransactionalService {
    private final AdminMapper adminMapper;
    private final SecurityUtil securityUtil;

    public void ExecutePetsitterApp(String token,String status,String user_no) {
        if(status == null || status.isEmpty() || status.equals("0")){
            throw new CommonException(CommonErrorCode.INVALID_PARAMETER);
        }
        securityUtil.getValidUserNo(token); //관리자 권한 검증
        adminMapper.updateSitterAppStatus(user_no,status);
        if(status.equals("1")){ //허가
            adminMapper.insertSitter(user_no);
            adminMapper.updateAuthorities(user_no,"ROLE_SITTER");
        }
    }
}
