package com.lms.onlinelms.usermanagement.controller;


import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import com.lms.onlinelms.usermanagement.mapper.IAdminMapper;
import com.lms.onlinelms.usermanagement.model.Admin;
import com.lms.onlinelms.usermanagement.service.interfaces.IAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AdminController {


    private final IAdminService adminService;
    private final IAdminMapper adminMapper;

    @GetMapping("/admins/{studentId}")
    public ResponseEntity<AdminUpdateDto> getStudent(@PathVariable Long studentId) {
        Admin admin = adminService.getAdminInfoById(studentId);

        AdminUpdateDto adminDto = adminMapper.toAdminDto(admin);

        return ResponseEntity.ok(adminDto);
    }



    @PutMapping("/admins/{adminId}/update")
    public ResponseEntity<AdminUpdateDto> updateAdmin(@RequestPart("adminInfo") @Valid AdminUpdateDto adminUpdateDto,
                                              @RequestPart("image") MultipartFile profileImage,
                                              @PathVariable Long adminId) {

        Admin admin  = adminService.updateAdmin(adminUpdateDto ,profileImage, adminId);


        return ResponseEntity.ok(adminMapper.toAdminDto(admin));
    }


}
