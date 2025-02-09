package com.lms.onlinelms.usermanagement.service.implementation;


import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import com.lms.onlinelms.usermanagement.dto.AdminUpdateDto;
import com.lms.onlinelms.usermanagement.model.Admin;
import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.repository.AdminRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.IAdminService;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService implements IAdminService {

private final IUserService userService;
private final AdminRepository adminRepository;
    private final IMediaService mediaService;


    @Override
    public void updateAdmin(AdminUpdateDto adminUpdateDto, MultipartFile profileImage, Long adminId) {

        Admin admin = findAdminById(adminId);

        userService.checkIfUserIdCorrect(adminId);

        admin.setFirstName(adminUpdateDto.getFirstName());
        admin.setLastName(adminUpdateDto.getLastName());
        admin.setProfileImage(adminUpdateDto.getProfileImage());
        admin.setPhoneNumber(adminUpdateDto.getPhoneNumber());


        String imageUrl = mediaService.saveFile(profileImage , "/ProfileImages");
        Image image= new Image();
        image.setName(profileImage.getOriginalFilename());
        image.setType(profileImage.getContentType());
        image.setImageUrl(imageUrl);
        admin.setProfileImage(image);

        adminRepository.save(admin);
    }


    private Admin findAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found.", HttpStatus.NOT_FOUND));
    }

}
