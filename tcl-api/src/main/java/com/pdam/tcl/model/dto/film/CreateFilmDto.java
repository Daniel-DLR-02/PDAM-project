package com.pdam.tcl.model.dto.film;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateFilmDto {

    @NotBlank
    @NotNull
    private String title;
    private String description;
    private String duration;
    private LocalDate releaseDate;
    private LocalDate expirationDate;
    private String genre;
}
