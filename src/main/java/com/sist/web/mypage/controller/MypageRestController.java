package com.sist.web.mypage.controller;

import com.sist.web.common.response.ApiResponse;
import com.sist.web.mypage.service.MypageService;
import com.sist.web.mypage.service.MypageTransactionalService;
import com.sist.web.mypage.vo.PetDTO;
import com.sist.web.mypage.vo.SitterDTO;
import com.sist.web.security.JwtTokenProvider;
import com.sist.web.user.mapper.UserMapper;
import com.sist.web.user.vo.UserDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/mypage")
public class MypageRestController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final MypageService mypageService;
    private final MypageTransactionalService mypageTransactionalService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDetailDTO>> GetMyinfo(@CookieValue(value="accessToken", required = false) String token) {
        UserDetailDTO dto = mypageService.getMyinfo(token);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> UpdateMyinfo(
            @ModelAttribute UserDetailDTO dto,
            @RequestParam(value="profile", required = false)MultipartFile file,
            @RequestParam(value="profileChange")int isChange,
            @CookieValue(value="accessToken", required = false) String token) {
        log.debug(dto.toString());
        mypageService.updateMyinfo(token,dto,file,isChange);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pets")
    public ResponseEntity<ApiResponse<List<PetDTO>>> GetMyPets(@CookieValue(value="accessToken", required = false) String token) {
        List<PetDTO> list = mypageService.getMyPets(token);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/pets/{petno}")
    public ResponseEntity<ApiResponse<PetDTO>> GetMyPetDetail(@PathVariable("petno") String petno, @CookieValue(value="accessToken", required = false) String token){
        PetDTO dto = mypageService.getMyPetDetail(token, petno);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PutMapping("/pets/{petno}")
    public ResponseEntity<Void> UpdateMyPetDetail(
            @ModelAttribute PetDTO dto,
            @RequestParam(value="pet_profilepic", required = false)MultipartFile file,
            @RequestParam(value="profileChange")int isChange,
            @CookieValue(value="accessToken", required = false) String token){
        mypageService.updateMyPetDetail(token,dto,file,isChange);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pets")
    public ResponseEntity<Void> InsertMyPetDetail(
            @ModelAttribute PetDTO dto,
            @RequestParam(value="profilepic", required = false)MultipartFile file,
            @RequestParam(value="profileChange")int isChange,
            @CookieValue(value="accessToken", required = false) String token){
        mypageTransactionalService.insertMyPetDetail(token,dto,file,isChange);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/petsitters")
    public ResponseEntity<Void> applyPetsitter(
            @ModelAttribute SitterDTO dto,
            @CookieValue(value="accessToken", required = false) String token){
        mypageService.applyOrUpdatePetsitter(token, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/petsitters")
    public ResponseEntity<ApiResponse<SitterDTO>> GetPetsitter(
            @CookieValue(value="accessToken", required = false) String token){
        return ResponseEntity.ok(ApiResponse.success(mypageService.getPetsitter(token)));
    }

}
