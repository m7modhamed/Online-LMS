package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface IAdminService {

    void updateAdmin(AdminUpdateDto adminUpdateDto, MultipartFile profileImage, Long adminId);
}
