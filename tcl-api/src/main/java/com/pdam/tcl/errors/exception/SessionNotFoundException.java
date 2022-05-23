package com.pdam.tcl.errors.exception;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public SessionNotFoundException(String message) {
        super(message);
    }
}