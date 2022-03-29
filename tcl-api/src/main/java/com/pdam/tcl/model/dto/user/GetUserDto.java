package com.pdam.tcl.model.dto.user;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetUserDto {

    private String nick;
    private String nombre;
    private LocalDate fechaDeNacimiento;
    private String email;
    private String avatar;
}
