package com.sist.web.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.web.common.exception.code.UserErrorCode;
import com.sist.web.common.exception.domain.UserException;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.KakaoUserDTO;
import com.sist.web.user.vo.UserDetailDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserTransactionalService userTransactionalService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> loginUser(UserVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_mail", vo.getUser_mail());
        int isUser = userMapper.getUserMailCount(vo.getUser_mail());
        if(isUser == 0) {
            map.put("state", "noid");
        }else{
            String password = vo.getPassword();
            System.out.println("orig: "+vo.getPassword());

            String dbPassword = userMapper.getPassword(vo.getUser_mail());
            System.out.println("db: "+dbPassword);
            if(passwordEncoder.matches(password,dbPassword)) {
                map.put("state", "ok");
                map.put("user_mail", vo.getUser_mail());
            }else{
                map.put("state", "no");
            }
        }
        System.out.println("login api : "+map);
        return map;
    }

    @Override
    public String getKakaoAccessToken(String code, String url) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type","authorization_code");
        requestBody.add("client_id","edc96d6c4e60c395ff9312d2ed6f71ba");

        requestBody.add("redirect_uri",url+"/auth/join");
        requestBody.add("code",code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<?> response=restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                requestEntity,
                Map.class
            );
            Map map= (Map)response.getBody();
            return (String)map.get("access_token");
        }catch (HttpClientErrorException e){
            log.error("[KAKAO ERROR BODY] " + e.getResponseBodyAsString());
            throw e;
        }
    }
    @Override
    public void insertOrLoginKakaoUser(String kakaoAccessToken, HttpServletResponse res) {
        //kakao 유저 정보 가져오기 api
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + kakaoAccessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                String.class
        );
        if(response.getStatusCode() != HttpStatus.OK) {
            log.error("[KAKAO ERROR BODY] " + response.getBody());
            throw new UserException(UserErrorCode.NOT_FOUND);
        }

        KakaoUserDTO kakaoUserDTO;
        try{
            kakaoUserDTO = objectMapper.readValue(response.getBody(), KakaoUserDTO.class);
            Long social_id= Objects.requireNonNull((kakaoUserDTO).getId());
            String profile=kakaoUserDTO.getProperties().getThumbnail_image();
            String nickname=kakaoUserDTO.getProperties().getNickname();
            String email=kakaoUserDTO.getKakao_account().getEmail();

            UserVO userVO=new UserVO();
            userVO.setNickname(nickname);
            userVO.setUser_name(nickname);
            userVO.setUser_mail(email);
            userVO.setSocial_id(social_id.toString());
            userVO.setProfile(profile);

            //중복가입 여부 확인
            int count = userMapper.getUserMailCount(email);

            if(count == 0) { //중복 없으면 가입
                userTransactionalService.insertKakaoUser(userVO);
            } else {
                //동일한 메일이 이미 가입되어 있으면 소셜 연동 여부 먼저 확인
                String socialId=userMapper.getSocialId(email);
                if(socialId != null) { //연동 되었다면
                    UserVO loginVO=userMapper.getUserVOFromSocialId(socialId);
                    log.debug("Authority:{}",loginVO.getAuthority());
                    // 토큰 발급 시작
                    List<String> roles = new ArrayList<>(Collections.singletonList(loginVO.getAuthority()));
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    //user 객체 생성
                    User principal = new User(String.valueOf(loginVO.getUser_no()), "", authorities);

                    String accessToken = jwtTokenProvider.createToken(String.valueOf(loginVO.getUser_no()), principal.getAuthorities());
                    String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(loginVO.getUser_no()));

                    res.addCookie(addCk("accessToken", accessToken, 1*60*60*24*7)); //쿠키 유효기간 7일
                    res.addCookie(addCk("refreshToken", refreshToken, 1*60*60*24*7));
                }else {
                    //소셜 연동되지 않았다면
                    throw new UserException(UserErrorCode.CONFLICT_EMAIL);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            throw new UserException(UserErrorCode.NOT_FOUND);
        }
    }

    @Override
    public void checkActiveUser(String userno) {
        int isActive=userMapper.checkUserActive(userno);
        if(!Integer.valueOf(1).equals(isActive)) {
            throw new UserException(UserErrorCode.NOT_FOUND);
        }
    }

    @Override
    public UserDetailDTO getUserDetail(String userno) {
        return userMapper.getUserDtoFromUserNo(userno);
    }

    @Override
    public UserDetailDTO getActiveUserDetail(String userno) {
        checkActiveUser(userno);
        return getUserDetail(userno);
    }

    @Override
    public UserDetailDTO getHeaderDetail(String token) {
        String userno=getValidUserNo(token);
        return userMapper.getHeaderDetailFromUserNo(userno);
    }

    public String getValidUserNo(String token) {
        if(token!=null){
            String userno=jwtTokenProvider.getUserNoFromToken(token);
            return userno;
        }
        return "";
    }

    Cookie addCk(String name, String token, int expire) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(expire);//1시간
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
