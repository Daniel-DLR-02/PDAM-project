package com.pdam.tcl.utils.converters;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.CreateFilmDto;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.img.ImgResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmDtoConverter {

    public GetFilmDto filmToGetFilmDto(Film film){
        return GetFilmDto.builder()
        .title(film.getTitle())
                .uuid(film.getUuid())
                .poster(film.getPoster().getLink())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .expirationDate(film.getExpirationDate())
                .genre(film.getGenre())
                .build();
    }

    public Film createFilmToFilm(CreateFilmDto createFilm, ImgResponse img){
        return Film.builder()
                .title(createFilm.getTitle())
                .poster(img.getData())
                .description(createFilm.getDescription())
                .duration(createFilm.getDuration())
                .releaseDate(createFilm.getReleaseDate())
                .expirationDate(createFilm.getExpirationDate())
                .genre(createFilm.getGenre())
                .build();

    }

}
