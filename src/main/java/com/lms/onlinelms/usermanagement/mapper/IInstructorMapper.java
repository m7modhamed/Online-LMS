package com.lms.onlinelms.usermanagement.mapper;

import com.lms.onlinelms.usermanagement.dto.InstructorSignupDto;
import com.lms.onlinelms.usermanagement.dto.StudentSignupDto;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import org.mapstruct.Mapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface IInstructorMapper {


    default Instructor toInstructor(InstructorSignupDto instructorSignupDto){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if ( instructorSignupDto == null ) {
            return null;
        }

        Instructor instructor = new Instructor();
        instructor.setFirstName(instructorSignupDto.getFirstName());
        instructor.setLastName(instructorSignupDto.getLastName());
        instructor.setEmail(instructorSignupDto.getEmail());
        instructor.setMyPassword(passwordEncoder.encode(instructorSignupDto.getPassword()));
        instructor.setPhoneNumber(instructorSignupDto.getPhoneNumber());
        instructor.setProfileImage(instructorSignupDto.getProfileImage());
        instructor.setAboutMe(instructorSignupDto.getAboutMe());
        instructor.setFacebookUrl(instructorSignupDto.getFacebookUrl());
        instructor.setGithubUrl(instructorSignupDto.getGithubUrl());
        instructor.setLinkedinUrl(instructorSignupDto.getLinkedinUrl());
        instructor.setTwitterUrl(instructorSignupDto.getTwitterUrl());
        instructor.setSpecialization(instructorSignupDto.getSpecialization());
        instructor.setIsActive(false);
        instructor.setIsBlocked(false);
        return instructor;
    };
}
