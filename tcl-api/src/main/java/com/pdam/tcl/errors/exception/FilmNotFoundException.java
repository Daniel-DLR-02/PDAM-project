package com.pdam.tcl.errors.exception;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }

    public FilmNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}