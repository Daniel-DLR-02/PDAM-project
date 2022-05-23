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
                .uuid(film.getUuid())
                .poster(film.getPoster().getLink())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .genre(film.getGenre())
                .build();
    }

}
