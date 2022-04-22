package com.pdam.tcl.utils.converters;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmDtoConverter {

    public GetFilmDto filmToGetFilmDto(Film film){
        return GetFilmDto.builder()
        .title(film.getTitle())
                .poster(film.getPoster())
                .description(film.getDescription())
                .duration(film.getDuration())
                .genre(film.getGenre())
                .build();
    }

}
