package com.lms.onlinelms.coursemanagement.service.implementation;

import com.lms.onlinelms.coursemanagement.service.interfaces.IStudentService;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;


    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
