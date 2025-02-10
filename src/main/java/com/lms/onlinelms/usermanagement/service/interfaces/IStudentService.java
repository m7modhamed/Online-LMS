package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.dto.StudentUpdateDto;
import com.lms.onlinelms.usermanagement.model.Student;
import org.springframework.web.multipart.MultipartFile;

public interface IStudentService {

    void saveStudent(Student student);

    Student updateStudent(StudentUpdateDto studentUpdateDto, MultipartFile profileImage, Long studentId);

    Student getStudentInfoById(Long studentId);
}
