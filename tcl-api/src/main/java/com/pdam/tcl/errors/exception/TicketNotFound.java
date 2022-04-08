package com.pdam.tcl.errors.exception;

public class TicketNotFound extends RuntimeException{

    public TicketNotFound(String message, Exception e) {
        super(message, e);
    }

    public TicketNotFound(String message) {
        super(message);
    }
}
