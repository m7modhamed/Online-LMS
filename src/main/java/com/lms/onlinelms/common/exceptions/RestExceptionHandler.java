package com.lms.onlinelms.common.exceptions;

import com.lms.onlinelms.coursemanagement.exception.CourseAccessException;
import com.lms.onlinelms.usermanagement.dto.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(value = {AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(WebRequest request) {
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage("you can not access this resource");
        errorResponse.setDetails(request.getDescription(false));
        errorResponse.setStatus(HttpStatus.FORBIDDEN);

       return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex , WebRequest request) {
        ErrorResponse errorResponse=new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDetails(request.getDescription(false));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);


        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    //handle validation exception for parameter level
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, String> HandlerMethodValidationException(HandlerMethodValidationException ex){
        Map<String, String> errors = new HashMap<>();

        // Process each validation error message
        ex.getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.put("message", errorMessage);
        });

        return errors;

    }

}