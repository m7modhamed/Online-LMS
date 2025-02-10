package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import com.lms.onlinelms.usermanagement.model.Admin;
import org.springframework.web.multipart.MultipartFile;

public interface IAdminService {

    Admin updateAdmin(AdminUpdateDto adminUpdateDto, MultipartFile profileImage, Long adminId);

    Admin getAdminInfoById(Long adminId);
}
