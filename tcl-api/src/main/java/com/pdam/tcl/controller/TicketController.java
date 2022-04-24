package com.pdam.tcl.controller;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.model.dto.user.GetUserDto;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final SessionService sessionService;

    @GetMapping("/{id}")
    public ResponseEntity<GetTicketDto> getOneTicket(@PathVariable UUID id) {

        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @PostMapping("/ticket")
    public ResponseEntity<GetTicketDto> buyTicket(@RequestBody CreateTicketDto newTicket,
                                                @AuthenticationPrincipal User currentUser){

        if(sessionService.existsByid(newTicket.getSessionUuid())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(newTicket, currentUser));
        }
        else{
            throw new SessionNotFoundException("Session not found.");
        }


    }

    
}
