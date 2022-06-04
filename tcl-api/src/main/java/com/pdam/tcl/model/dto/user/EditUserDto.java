package com.pdam.tcl.model.dto.user;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class EditUserDto {

    private String nombre;
    private String nickName;
    private String email;
    private LocalDate fechaNacimiento;

}
