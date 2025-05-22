package com.sist.web.util;

import com.sist.web.common.exception.code.CommonErrorCode;
import com.sist.web.common.exception.domain.CommonException;
import com.sist.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final JwtTokenProvider jwtTokenProvider;
    public String getValidUserNo(String token) {
        if(token==null){
            throw new CommonException(CommonErrorCode.SC_UNAUTHORIZED);
        }
        String userno=jwtTokenProvider.getUserNoFromToken(token);
        if(userno==null){
            throw new CommonException(CommonErrorCode.NOT_FOUND);
        }
        return userno;
    }
}
