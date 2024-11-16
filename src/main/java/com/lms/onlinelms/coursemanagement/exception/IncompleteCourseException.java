package com.lms.onlinelms.coursemanagement.exception;

import com.lms.onlinelms.common.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class IncompleteCourseException extends AppException {
    public IncompleteCourseException(String message, HttpStatus status) {
        super(message, status);
    }
}
