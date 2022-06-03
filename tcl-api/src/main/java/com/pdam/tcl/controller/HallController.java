package com.pdam.tcl.controller;


import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.dto.hall.CreateHallDto;
import com.pdam.tcl.service.HallService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hall")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    @GetMapping("/")
    public ResponseEntity<Page<Hall>> getAllHalls(@PageableDefault(size = 20) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(hallService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hall> findOne(@PathVariable UUID id) {
        return ResponseEntity.ok(hallService.findOne(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hall> update(@PathVariable UUID id, @RequestBody CreateHallDto createHallDto) {
        return ResponseEntity.ok(hallService.edit(id, createHallDto));
    }

    @PostMapping("/")
    public ResponseEntity<Hall> create(@RequestBody CreateHallDto hall) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hallService.save(hall));
    }
}
