package com.pdam.tcl.controller;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.utils.converters.SessionDtoConverter;
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
public class SessionController {

    private final SessionService sessionService;
    private final SessionDtoConverter sessionDtoConverter;


    @GetMapping("/{id}")
    public ResponseEntity<GetSessionDto> getSessions(@PathVariable("id") UUID id) {

        Session sesionBuscada = sessionService.findById(id).orElseThrow(()->new SessionNotFoundException("Session not found"));

        return ResponseEntity.ok(sessionDtoConverter.sessionToGetSessionDto(sesionBuscada));
    }

    @PostMapping("/")
    public ResponseEntity<GetSessionDto> createSession(@RequestBody CreateSessionDto createSessionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionDtoConverter.sessionToGetSessionDto(sessionService.save(createSessionDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetSessionDto> editSession(@PathVariable("id") UUID id, @RequestBody CreateSessionDto createSessionDto) {
        return ResponseEntity.ok(sessionDtoConverter.sessionToGetSessionDto(sessionService.edit(id, createSessionDto)));
    }

    @GetMapping("/film/{filmId}")
    public ResponseEntity<Page<GetSessionDto>> getSessionByFilmId(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request, @PathVariable("filmId") UUID filmId) {
        return ResponseEntity.ok(sessionService.findSessionsByFilmId(filmId,pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable UUID id) {

        sessionService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<GetSessionDto>> getAllSessions(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(sessionService.getAllSessions(pageable));
    }

}
