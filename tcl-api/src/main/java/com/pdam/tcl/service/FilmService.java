package com.pdam.tcl.service;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface FilmService {
    Film save(CreateFilmDto createFilm, MultipartFile file) throws Exception;

    Optional<Film> findById(UUID id);

    Film getFilm(UUID id);

    void delete(UUID id) throws IOException;

    Film update(UUID id, CreateFilmDto editFilm, MultipartFile file) throws Exception;

    Page<GetFilmDto> getCurrentFilms(Pageable pageable);

    Page<GetFilmDto> getAllFilms(Pageable pageable);


}
