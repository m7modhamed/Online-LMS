package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.dto.InstructorUpdateDto;
import com.lms.onlinelms.usermanagement.model.Instructor;
import org.springframework.web.multipart.MultipartFile;

public interface IInstructorService {

    void updateInstructor(InstructorUpdateDto instructorUpdateDto, MultipartFile profileImage, Long instructorId);

    Instructor getInstructorInfoById(Long instructorId);
}
