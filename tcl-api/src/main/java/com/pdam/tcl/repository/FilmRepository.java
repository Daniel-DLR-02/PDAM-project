package com.pdam.tcl.repository;

import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FilmRepository extends JpaRepository<Film, UUID> {

    @Query(value = """
                        select new com.pdam.tcl.model.dto.film.GetFilmDto(
                            f.uuid,f.title,CAST(f.poster as string),f.description,f.duration,f.releaseDate,f.expirationDate,f.genre
                        ) from Film f
                        where CURRENT_DATE between f.releaseDate and f.expirationDate
                        order by f.releaseDate DESC
                    """)
    Page<GetFilmDto> findCurrentFilms(Pageable pageable);

    @Query(value = """
                        select new com.pdam.tcl.model.dto.film.GetFilmDto(
                            f.uuid,f.title,CAST(f.poster as string),f.description,f.duration,f.releaseDate,f.expirationDate,f.genre
                        ) from Film f
                        order by f.releaseDate DESC
                    """)
    Page<GetFilmDto> getAllFilms(Pageable pageable);

}
