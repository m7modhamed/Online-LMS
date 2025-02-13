package com.lms.onlinelms.usermanagement.service.implementation;

import com.lms.onlinelms.common.exceptions.AppException;
import com.lms.onlinelms.common.exceptions.ResourceNotFoundException;
import com.lms.onlinelms.usermanagement.dto.*;
import com.lms.onlinelms.usermanagement.event.RegistrationCompleteEvent;
import com.lms.onlinelms.usermanagement.event.listener.RegistrationCompleteEventListener;
import com.lms.onlinelms.usermanagement.mapper.IInstructorMapper;
import com.lms.onlinelms.usermanagement.mapper.IStudentMapper;
import com.lms.onlinelms.usermanagement.model.*;
import com.lms.onlinelms.usermanagement.repository.*;
import com.lms.onlinelms.usermanagement.service.interfaces.IAuthService;
import com.lms.onlinelms.usermanagement.service.interfaces.ITokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final IStudentMapper studentMapper;
    private final IInstructorMapper instructorMapper;
    private final RoleRepository roleRepository;
    private final InstructorRepository instructorRepository;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher publisher;
    private final ITokenService tokenService;
    private final RegistrationCompleteEventListener eventListener;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public Student signupStudent(StudentSignupDto studentSignupDto, HttpServletRequest request) {

        throwExceptionIfUserExists(studentSignupDto.getEmail());


        Student student = studentMapper.toStudent(studentSignupDto);
        Optional<Role> studentRole = roleRepository.findByName("ROLE_STUDENT");
        if(studentRole.isPresent()) {
            student.setRole(studentRole.get());
        }else{
            throw new ResourceNotFoundException("the role not found",HttpStatus.NOT_FOUND);
        }

        Student savedStudent=studentRepository.save(student);
        publisher.publishEvent(new RegistrationCompleteEvent(savedStudent, request.getHeader("Origin")));
        return savedStudent;
    }

    @Override
    public Instructor signupInstructor(InstructorSignupDto instructorSignupDto, HttpServletRequest request) {
        throwExceptionIfUserExists(instructorSignupDto.getEmail());

        Instructor instructor=instructorMapper.toInstructor(instructorSignupDto);

        Optional<Role> instructorRole = roleRepository.findByName("ROLE_INSTRUCTOR");
        if(instructorRole.isPresent()) {
            instructor.setRole(instructorRole.get());
        }else {
            throw new ResourceNotFoundException("the role not found",HttpStatus.NOT_FOUND);
        }
        Instructor savedInstructor = instructorRepository.save(instructor);
        publisher.publishEvent(new RegistrationCompleteEvent(savedInstructor, request.getHeader("Origin")));
        return savedInstructor;
    }

    public User login(LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );
        return userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow();
    }


    private void throwExceptionIfUserExists(String email) {
        Optional<User> user= userRepository.findByEmail(email);

        if(user.isPresent()) {
            throw new AppException("the user already exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> activateUser(String token) {
        Token tokenObj = tokenService.validateToken(token);

        User user = tokenObj.getUser();
        if (user.getIsActive()) {
            throw new AppException( "The user account is already active." ,HttpStatus.CONFLICT);
        }

        user.setIsActive(true);
        userRepository.save(user);
        tokenObj.setUsed(true);
        tokenService.saveToken(tokenObj);
        return ResponseEntity.ok("account verify successfully");
    }

    @Override
    public void resetPasswordRequest(User user, String originUrl) {
        String passwordResetToken = UUID.randomUUID().toString();
        tokenService.createPasswordResetTokenForUser(user, passwordResetToken);

        // Send password reset verification email to the user
        String urlWithToken = originUrl + "/auth/resetPassword/" + passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(urlWithToken, user);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new AppException("Error sending password reset email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        Token tokenObj=tokenService.validateToken(token);
        User account=tokenObj.getUser();
        String encodedPassword = passwordEncoder.encode(newPassword);


        account.setMyPassword(encodedPassword);
        userRepository.save(account);

        tokenObj.setUsed(true);
        tokenService.saveToken(tokenObj);

        return "Password reset successfully";
    }

    @Override
    public void toggleBlockStatus(Long userId) {
        User user = userService.getUserById(userId);
        user.setIsBlocked(!user.getIsBlocked());
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found.", HttpStatus.NOT_FOUND));
    }


}
