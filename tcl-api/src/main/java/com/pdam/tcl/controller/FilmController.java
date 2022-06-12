package com.pdam.tcl.controller;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.utils.converters.FilmDtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
@Tag(name = "Films", description = "Esta clase implementa los endpoints para Films.")
public class FilmController {

    private final FilmService service;
    private final FilmDtoConverter filmDtoConverter;

    @Operation(summary = "Registra una película en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente la película",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFilmDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear la película.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @PostMapping("/")
    public ResponseEntity<GetFilmDto> create(@RequestPart("film") CreateFilmDto film, @RequestPart("file") MultipartFile poster) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmDtoConverter.filmToGetFilmDto(service.save(film,poster)));
    }

    @Operation(summary = "Obtiene una película en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la película correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFilmDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La película buscada no se encuentra registrada en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetFilmDto> getOne(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.getFilm(id)));
    }

    @Operation(summary = "Lista todas las películas que se encuentran activas, es decir, cuya fecha de estreno es anterior a hoy, y su fecha de caducidad es posterior a hoy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han listado las películas correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFilmDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/active")
    public ResponseEntity<Page<GetFilmDto>> getCurrentFilms(@PageableDefault(size = 30) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(service.getCurrentFilms(pageable));
    }

    @Operation(summary = "Lista todas las películas registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han listado las películas correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFilmDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<GetFilmDto>> getAllFilms(@PageableDefault(size = 30) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok(service.getAllFilms(pageable));
    }

    @Operation(summary = "Edita una película en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente la película buscada.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFilmDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una película cuyo UUID coincida con el especificado en la url.",
                    content = @Content),

            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar la pelicula registrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),

    })
    @PutMapping("/{id}")
    public ResponseEntity<GetFilmDto> update(@PathVariable("id") UUID id, @RequestPart("film") CreateFilmDto film,@Nullable @RequestPart("file") MultipartFile poster) throws Exception {
        if(poster==null || poster.isEmpty()) {
            return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.updateNoAvatar(id, film)));
        }
        else{
            return ResponseEntity.ok(filmDtoConverter.filmToGetFilmDto(service.update(id, film, poster)));
        }
    }

    @Operation(summary = "Elimina una película especificada por url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado la película especificada de manera idempotente.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws IOException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
