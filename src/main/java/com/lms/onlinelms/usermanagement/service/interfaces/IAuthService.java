package com.lms.onlinelms.usermanagement.service.interfaces;

import com.lms.onlinelms.usermanagement.dto.*;
import com.lms.onlinelms.usermanagement.model.Instructor;
import com.lms.onlinelms.usermanagement.model.Student;
import com.lms.onlinelms.usermanagement.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {

    User login(LoginRequestDto loginRequestDto);
    Student signupStudent(StudentSignupDto studentSignupDto, HttpServletRequest request) ;
    Instructor signupInstructor(InstructorSignupDto instructorSignupDto, HttpServletRequest request) ;
    User findByEmail(String email);
    ResponseEntity<String> activateUser(String token);
    void resetPasswordRequest(User user, String originUrl);

    String resetPassword(String token, String password);

    void toggleBlockStatus(Long userId);
}
