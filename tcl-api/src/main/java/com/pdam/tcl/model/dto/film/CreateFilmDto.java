package com.pdam.tcl.model.dto.film;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateFilmDto {

    private String title;
    private String description;
    private String duration;
    private LocalDate releaseDate;
    private LocalDate expirationDate;
    private String genre;
}
