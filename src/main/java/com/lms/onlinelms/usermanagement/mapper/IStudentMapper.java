package com.lms.onlinelms.usermanagement.mapper;

import com.lms.onlinelms.usermanagement.dto.StudentSignupDto;
import com.lms.onlinelms.usermanagement.model.Student;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface IStudentMapper {


    default Student toStudent(StudentSignupDto studentSignupDto){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if ( studentSignupDto == null ) {
            return null;
        }

        Student student = new Student();
        student.setFirstName(studentSignupDto.getFirstName());
        student.setLastName(studentSignupDto.getLastName());
        student.setEmail(studentSignupDto.getEmail());
        student.setMyPassword(passwordEncoder.encode(studentSignupDto.getPassword()));
        student.setPhoneNumber(studentSignupDto.getPhoneNumber());
        student.setProfileImage(studentSignupDto.getProfileImage());
        student.setIsActive(false);
        student.setIsBlocked(false);
        return student;
    }

}