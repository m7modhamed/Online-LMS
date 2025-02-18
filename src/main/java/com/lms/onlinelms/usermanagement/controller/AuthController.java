package com.lms.onlinelms.usermanagement.controller;

import com.lms.onlinelms.usermanagement.dto.*;
import com.lms.onlinelms.usermanagement.model.User;
import com.lms.onlinelms.usermanagement.security.UserAuthenticationProvider;
import com.lms.onlinelms.usermanagement.service.interfaces.*;
import com.lms.onlinelms.usermanagement.validation.customAnnotations.Password;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;


    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        User user = authService.login(loginRequestDto);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setStatus("success");
        loginResponseDto.setMessage("Login successful");

        String accessToken = userAuthenticationProvider.createAccessToken(user);
        String refreshToken = userAuthenticationProvider.createRefreshToken(user);

        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/register/student")
    public ResponseEntity<String> signupStudent(@RequestBody @Valid StudentSignupDto studentSignupDto, HttpServletRequest request) {
        User user = authService.signupStudent(studentSignupDto, request);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body("User registered successfully. Please check your email to verify your account.");
    }


    @PostMapping("/register/instructor")
    public ResponseEntity<String> signupInstructor(@RequestBody @Valid InstructorSignupDto instructorSignupDto, HttpServletRequest request) {
        User user = authService.signupInstructor(instructorSignupDto, request);

        return ResponseEntity.created(URI.create("/users/" + user.getId())).body("User registered successfully. Please check your email to verify your account.");
    }


    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        return authService.activateUser(token);
    }

    @GetMapping("/forgot-password-request")
    public ResponseEntity<String> resetPasswordRequest(
            @RequestParam @NotBlank @Email String email
            , HttpServletRequest request) {

        User user = authService.findByEmail(email);
        authService.resetPasswordRequest(user, request.getHeader("Origin"));

        // Assuming the service method completes without exceptions, return success response.
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam @NotBlank String token, @RequestParam @NotBlank @Password String password) {
        String resetResult = authService.resetPassword(token, password);
        HttpStatus status = resetResult.equals("Password reset successfully") ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(resetResult);
    }


    @GetMapping("/users/{userId}/toggleBlock")
    public ResponseEntity<String> toggleBlockStatus(@PathVariable Long userId) {

        authService.toggleBlockStatus(userId);

        return ResponseEntity.ok("User Blocked/Unblocked Successfully");
    }


    @PostMapping("/refresh_Token")
    public ResponseEntity<LoginResponseDto> refreshToken(HttpServletRequest request) {
        User user = authService.refreshToken(request);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setStatus("success");
        loginResponseDto.setMessage("re-validate access token successful");

        String accessToken = userAuthenticationProvider.createAccessToken(user);
        String refreshToken = userAuthenticationProvider.createRefreshToken(user);

        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDto);
    }


}
