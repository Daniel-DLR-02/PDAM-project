package com.pdam.tcl.errors.exception;

public class SeatOccupiedException extends RuntimeException {

    public SeatOccupiedException(String message, Exception e) {
        super(message, e);
    }

    public SeatOccupiedException(String message) {
        super(message);
    }
}