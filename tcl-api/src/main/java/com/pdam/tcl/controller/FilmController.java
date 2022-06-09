package com.pdam.tcl.controller;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.utils.converters.FilmDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;
    private final FilmDtoConverter filmDtoConverter;

    @PostMapping("/")
    public ResponseEntity<GetFilmDto> create(@RequestPart("film") CreateFilmDto film, @RequestPart("file") MultipartFile poster) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmDtoConverter.filmToGetFilmDto(service.save(film,poster)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetFilmDto> getOne(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.getFilm(id)));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<GetFilmDto>> getCurrentFilms(@PageableDefault(size = 30) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(service.getCurrentFilms(pageable));
    }

    @GetMapping("/")
    public ResponseEntity<Page<GetFilmDto>> getAllFilms(@PageableDefault(size = 30) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(service.getAllFilms(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetFilmDto> update(@PathVariable("id") UUID id, @RequestPart("film") CreateFilmDto film, @RequestPart("file") MultipartFile poster) throws Exception {
        if(poster==null || poster.isEmpty()) {
            return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.updateNoAvatar(id, film)));
        }
        else{
            return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.update(id, film, poster)));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws IOException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
