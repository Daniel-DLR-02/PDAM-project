package com.pdam.tcl.controller;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.utils.converters.SessionDtoConverter;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Esta clase implementa los endpoints para Session")
public class SessionController {

    private final SessionService sessionService;
    private final SessionDtoConverter sessionDtoConverter;

    @Operation(summary = "Obtiene una sesión registradas en la base de datos en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la sesión buscada correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSessionDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La sesión buscada no se encuentra registrada en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetSessionDto> getSession(@PathVariable("id") UUID id) {

        Session sesionBuscada = sessionService.findById(id).orElseThrow(()->new SessionNotFoundException("Session not found"));

        return ResponseEntity.ok(sessionDtoConverter.sessionToGetSessionDto(sesionBuscada));
    }

    @Operation(summary = "Registra una sesión en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente la sesión correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSessionDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear la sesión.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<GetSessionDto> createSession(@RequestBody CreateSessionDto createSessionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionDtoConverter.sessionToGetSessionDto(sessionService.save(createSessionDto)));
    }

    @Operation(summary = "Edita una sesión en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente la sesión buscada.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSessionDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una sesión cuyo UUID coincida con el especificado en la url.",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar la sesión registrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @PutMapping("/{id}")
    public ResponseEntity<GetSessionDto> editSession(@PathVariable("id") UUID id, @RequestBody CreateSessionDto createSessionDto) {
        return ResponseEntity.ok(sessionDtoConverter.sessionToGetSessionDto(sessionService.edit(id, createSessionDto)));
    }

    @Operation(summary = "Obtiene todas las sesiones registradas en la base de datos, cuya película coincida con la especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han listado las sesiones correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSessionDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/film/{filmId}")
    public ResponseEntity<Page<GetSessionDto>> getSessionByFilmId(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request, @PathVariable("filmId") UUID filmId) {
        return ResponseEntity.ok(sessionService.findSessionsByFilmId(filmId,pageable));
    }

    @Operation(summary = "Elimina una sesión especificada por url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado la sesión especificada de manera idempotente.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable UUID id) {

        sessionService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtiene todas las sesiones registradas en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han listado las sesiones correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetSessionDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<GetSessionDto>> getAllSessions(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(sessionService.getAllSessions(pageable));
    }

}
