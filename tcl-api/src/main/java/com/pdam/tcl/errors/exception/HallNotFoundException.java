package com.pdam.tcl.errors.exception;

public class HallNotFoundException extends RuntimeException {

    public HallNotFoundException(String message) {
        super(message);
    }

    public HallNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}