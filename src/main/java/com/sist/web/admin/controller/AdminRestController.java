package com.sist.web.admin.controller;

import com.sist.web.admin.dto.AdminSitterAppDetailDTO;
import com.sist.web.admin.service.AdminTransactionalService;
import com.sist.web.mypage.vo.PetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminTransactionalService adminService;

    @PostMapping("/api/admin/petsitters/{userno:[0-9]+}/applications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> petsitterAppExecute(@CookieValue(value="accessToken", required = true) String token,
                                                    @RequestBody AdminSitterAppDetailDTO dto,
                                                    @PathVariable(value="userno")String user_no) {
        adminService.ExecutePetsitterApp(token, dto.getStatus(), user_no);
        return ResponseEntity.ok().build();

    }
}
