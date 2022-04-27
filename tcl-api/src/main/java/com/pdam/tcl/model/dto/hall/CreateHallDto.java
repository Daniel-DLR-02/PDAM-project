package com.pdam.tcl.model.dto.hall;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateHallDto {
    private String name;
}
