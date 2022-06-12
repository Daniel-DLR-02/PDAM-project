package com.pdam.tcl.controller;


import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.dto.hall.CreateHallDto;
import com.pdam.tcl.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hall")
@RequiredArgsConstructor
@Tag(name = "Halls", description = "Esta clase implementa los endpoints para Hall")
public class HallController {

    private final HallService hallService;


    @Operation(summary = "Obtiene todas las salas registradas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han listado las sala correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hall.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<Hall>> getAllHalls(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(hallService.findAll(pageable));
    }

    @Operation(summary = "Obtiene una sala en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la sala correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hall.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La sala buscada no se encuentra registrada en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Hall> findOne(@PathVariable UUID id) {
        return ResponseEntity.ok(hallService.findOne(id));
    }

    @Operation(summary = "Elimina una sala especificada por url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado la sala especificada de manera idempotente.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edita una sala en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente la sala buscada.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hall.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una sala cuyo UUID coincida con el especificado en la url.",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar la sala registrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @PutMapping("/{id}")
    public ResponseEntity<Hall> update(@PathVariable UUID id, @Valid @RequestBody CreateHallDto createHallDto) {
        return ResponseEntity.ok(hallService.edit(id, createHallDto));
    }

    @Operation(summary = "Registra una sala en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente la sala",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hall.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear la sala.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Hall> create(@Valid @RequestBody CreateHallDto hall) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hallService.save(hall));
    }
}
