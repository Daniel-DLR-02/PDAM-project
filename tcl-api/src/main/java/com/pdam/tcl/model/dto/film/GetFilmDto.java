package com.pdam.tcl.model.dto.film;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class GetFilmDto {
    private UUID uuid;
    private String title;
    private String poster;
    private String description;
    private String duration;
    private String genre;
}
