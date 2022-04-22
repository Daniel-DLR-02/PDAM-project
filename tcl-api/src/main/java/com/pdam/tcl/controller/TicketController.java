package com.pdam.tcl.controller;

import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/")
    public ResponseEntity<GetTicketDto> getOneTicket(@PathVariable UUID uuidTicket) {

        return ResponseEntity.ok(ticketService.getTicket(uuidTicket));
    }

    
}
