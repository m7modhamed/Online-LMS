package com.lms.onlinelms.common.exceptions;

import com.lms.onlinelms.coursemanagement.exception.CourseAccessException;
import com.lms.onlinelms.usermanagement.dto.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { InsufficientAuthenticationException.class })
    public ResponseEntity<LoginResponseDto> handleAuthenticationException(Exception ex) {

        LoginResponseDto loginResponseDto =new LoginResponseDto();

        loginResponseDto.setStatus("error");
        loginResponseDto.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);

    }

    @ExceptionHandler(value = {AccessDeniedException.class })
    public ProblemDetail handleAccessDeniedException() {
        ProblemDetail errorDetail;

        errorDetail=ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        errorDetail.setProperty("access-denied-reason","you can not access this resource");

        return errorDetail;
    }

    @ExceptionHandler(value = {BadCredentialsException.class })
    public ResponseEntity<LoginResponseDto> handleBadCredentialsException(BadCredentialsException ex) {

        LoginResponseDto loginResponseDto =new LoginResponseDto();
        loginResponseDto.setStatus("error");
        loginResponseDto.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
    }


    @ExceptionHandler(value = { DisabledException.class })
    public ResponseEntity<LoginResponseDto> handleException() {
        LoginResponseDto loginResponseDto =new LoginResponseDto();
        loginResponseDto.setStatus("inactive");
        loginResponseDto.setMessage("Account is inactive");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
    }


    @ExceptionHandler(value = { LockedException.class })
    public ResponseEntity<LoginResponseDto> handleLockedException() {
        LoginResponseDto loginResponseDto =new LoginResponseDto();
        loginResponseDto.setStatus("blocked");
        loginResponseDto.setMessage("Account is blocked");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponseDto);
    }


    @ExceptionHandler(value = { Exception.class })
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail errorDetail;

        errorDetail=ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        errorDetail.setProperty("access-denied-reason",ex.getMessage());

        return errorDetail;
    }


    @ExceptionHandler(value = { AppException.class })
    public ResponseEntity<ErrorResponse> handleAppException(AppException e , WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage() ,request.getDescription(false) , e.getStatus());

        return new ResponseEntity<>(errorResponse, e.getStatus());

    }

    @ExceptionHandler(value = CourseAccessException.class)
    public ResponseEntity<ErrorResponse> CourseAccessException(CourseAccessException e , WebRequest request){


        ErrorResponse errorResponse = new ErrorResponse(e.getMessage() ,request.getDescription(false) , e.getStatus());

        return new ResponseEntity<>(errorResponse, e.getStatus());
    }
}
