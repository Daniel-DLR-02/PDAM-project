package com.pdam.tcl.errors.exception;

public class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public RequestNotFoundException(String message) {
        super(message);
    }
}