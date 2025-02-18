package com.lms.onlinelms.usermanagement.exception;

public class MismatchTokenType extends RuntimeException {
    public MismatchTokenType() {
        super("Error, Mismatch Token type");
    }
}
