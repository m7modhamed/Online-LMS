package com.lms.onlinelms.usermanagement.controller;


import com.lms.onlinelms.coursemanagement.dto.CourseInfoDto;
import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import com.lms.onlinelms.usermanagement.dto.UserDto;
import com.lms.onlinelms.usermanagement.mapper.IAdminMapper;
import com.lms.onlinelms.usermanagement.mapper.IUserMapper;
import com.lms.onlinelms.usermanagement.model.Admin;
import com.lms.onlinelms.usermanagement.model.User;
import com.lms.onlinelms.usermanagement.service.implementation.UserService;
import com.lms.onlinelms.usermanagement.service.interfaces.IAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AdminController {


    private final IAdminService adminService;
    private final IAdminMapper adminMapper;
    private final UserService userService;
    private final IUserMapper userMapper;

    @GetMapping("/admins/{studentId}")
    public ResponseEntity<AdminUpdateDto> getStudent(@PathVariable Long studentId) {
        Admin admin = adminService.getAdminInfoById(studentId);

        AdminUpdateDto adminDto = adminMapper.toAdminDto(admin);

        return ResponseEntity.ok(adminDto);
    }


    @PutMapping("/admins/{adminId}/update")
    public ResponseEntity<AdminUpdateDto> updateAdmin(@RequestPart("adminInfo") @Valid AdminUpdateDto adminUpdateDto, @RequestPart("image") MultipartFile profileImage, @PathVariable Long adminId) {

        Admin admin = adminService.updateAdmin(adminUpdateDto, profileImage, adminId);


        return ResponseEntity.ok(adminMapper.toAdminDto(admin));
    }


    @GetMapping("/admins/{adminId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsers(@PathVariable Long adminId,
                                                     @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(value = "sortBy", required = false) String[] sortBy,
                                                     @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        userService.checkIfUserIdCorrect(adminId);

        if (sortBy == null || sortBy.length == 0) {
            sortBy = new String[]{"createdAt"};
        }
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(offset, pageSize, sort);

        Page<User> users = userService.getAllUsers(pageRequest);

        Page<UserDto> userDtos = users.map(userMapper::toUserDto);


        return ResponseEntity.ok(userDtos);
    }


}
