package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.coursemanagement.service.interfaces.IMediaService;
import com.lms.onlinelms.usermanagement.dto.StudentUpdateDto;
import com.lms.onlinelms.usermanagement.model.Image;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.repository.StudentRepository;
import com.lms.onlinelms.usermanagement.service.interfaces.IStudentService;
import com.lms.onlinelms.usermanagement.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;
    private final IUserService userService;



    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found.", HttpStatus.NOT_FOUND));
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void updateStudent(StudentUpdateDto studentUpdateDto, MultipartFile profileImage, Long studentId) {
        Student student= findStudentById(studentId);

        userService.checkIfUserIdCorrect(studentId);

        student.setFirstName(studentUpdateDto.getFirstName());
        student.setLastName(studentUpdateDto.getLastName());
        student.setProfileImage(studentUpdateDto.getProfileImage());
        student.setPhoneNumber(studentUpdateDto.getPhoneNumber());

    /*    String imageUrl = mediaService.saveFile(profileImage , "/ProfileImages");
        Image image= new Image();
        image.setName(profileImage.getOriginalFilename());
        image.setType(profileImage.getContentType());
        image.setImageUrl(imageUrl);
        student.setProfileImage(image);
*/

        studentRepository.save(student);
    }
}
