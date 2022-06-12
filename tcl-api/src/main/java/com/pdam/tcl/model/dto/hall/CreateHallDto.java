package com.pdam.tcl.model.dto.hall;

import com.pdam.tcl.validation.simple.anotations.UniqueHallName;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateHallDto {

    @UniqueHallName
    @NotNull
    @NotBlank
    private String name;

}
