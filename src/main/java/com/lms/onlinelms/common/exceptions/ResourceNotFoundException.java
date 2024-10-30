package com.lms.onlinelms.common.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AppException{


    public ResourceNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }


}
