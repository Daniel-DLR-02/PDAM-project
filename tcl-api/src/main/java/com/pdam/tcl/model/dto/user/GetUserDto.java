package com.pdam.tcl.model.dto.user;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetUserDto {

    private UUID uuid;
    private String nick;
    private String nombre;
    private LocalDate fechaDeNacimiento;
    private String email;
    private String avatar;
}
