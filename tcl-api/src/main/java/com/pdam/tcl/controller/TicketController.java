package com.pdam.tcl.controller;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.Ticket;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/")
    public ResponseEntity<GetTicketDto> buyTicket(@RequestBody CreateTicketDto newTicket,
                                                @AuthenticationPrincipal User currentUser){
        System.out.println(currentUser.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(newTicket, currentUser));



    }

    @GetMapping("/user")
    public ResponseEntity<Page<GetTicketDto>> getTicketsCurrentUser(@PageableDefault(size = 10) Pageable pageable, HttpServletRequest request, @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(ticketService.getTicketsByUserId(currentUser.getUuid(),pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable UUID id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetTicketDto> updateTicket(@PathVariable UUID id, @RequestBody CreateTicketDto newTicket){
        return ResponseEntity.ok(ticketService.editTicket(id, newTicket));
    }
}

