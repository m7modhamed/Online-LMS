package com.lms.onlinelms.coursemanagement.exception;

import com.lms.onlinelms.common.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class UnsuitableCourseStatusException extends AppException {
    public UnsuitableCourseStatusException(String message, HttpStatus status) {
        super(message, status);
    }
}
