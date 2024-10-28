package com.lms.onlinelms.coursemanagement.exception;

import com.lms.onlinelms.common.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class CourseAccessException extends AppException {
    public CourseAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
