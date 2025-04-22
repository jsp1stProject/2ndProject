package com.sist.web.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.KakaoUserDTO;
import com.sist.web.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public String GetKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type","authorization_code");
        requestBody.add("client_id","edc96d6c4e60c395ff9312d2ed6f71ba");
        requestBody.add("redirect_uri","http://localhost:8080/web/auth/join");
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
            System.out.println("[KAKAO ERROR BODY] " + e.getResponseBodyAsString());
            throw e;
        }
    }
    @Override
    public ResponseEntity InsertOrLoginKakaoUser(String kakaoAccessToken, HttpServletResponse res) {
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

        KakaoUserDTO kakaoUserDTO=null;
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

            //중복가입 여부 확인
            int count = userMapper.getUserMailCount(email);

            if(count == 0) {
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
                }






            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
    Cookie addCk(String name, String token, int expire) {
        Cookie cookie = new Cookie(name, token);
        cookie.setMaxAge(expire);//1시간
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
