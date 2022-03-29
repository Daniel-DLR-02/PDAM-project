package com.pdam.tcl.model.dto.film;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class GetFilmDto {
    private String title;
    private String poster;
    private String description;
    private String duration;
    private String genre;
}
