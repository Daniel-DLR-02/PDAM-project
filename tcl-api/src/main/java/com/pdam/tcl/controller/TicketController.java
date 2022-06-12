package com.pdam.tcl.controller;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.Ticket;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tickets", description = "Esta clase implementa los endpoints para Ticket")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Obtiene un ticket en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado el ticket correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "El ticket buscado no se encuentra registrado en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetTicketDto> getOneTicket(@PathVariable UUID id) {

        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @Operation(summary = "Registra un ticket en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente el ticket",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear el ticket.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<GetTicketDto> buyTicket(@RequestBody CreateTicketDto newTicket,
                                                @AuthenticationPrincipal User currentUser){
        System.out.println(currentUser.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(newTicket, currentUser));



    }

    @Operation(summary = "Obtiene todos los tickets de un usuario en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado los ticket del usuario correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario buscado no se encuentra registrado en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/user")
    public ResponseEntity<Page<GetTicketDto>> getTicketsCurrentUser(@PageableDefault(size = 30) Pageable pageable, HttpServletRequest request, @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(ticketService.getTicketsByUserId(currentUser.getUuid(),pageable));
    }

    @Operation(summary = "Elimina un ticket especificada por url en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el ticket especificado de manera idempotente.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable UUID id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edita un ticket en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente la ticket buscada.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado un ticket cuyo UUID coincida con el especificado en la url.",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar el ticket registrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @PutMapping("/{id}")
    public ResponseEntity<GetTicketDto> updateTicket(@PathVariable UUID id, @RequestBody CreateTicketDto newTicket){
        return ResponseEntity.ok(ticketService.editTicket(id, newTicket));
    }
}

