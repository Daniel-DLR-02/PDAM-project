package com.pdam.tcl.model.dto.user;

import com.pdam.tcl.validation.simple.anotations.UniqueEmail;
import com.pdam.tcl.validation.simple.anotations.UniqueNickname;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateUserDto {

    private String nombre;

    @UniqueNickname
    @NotNull
    @NotBlank(message = "El campo de nickname debe estar relleno obligatoriamente.")
    private String nickName;

    @UniqueEmail
    @Email
    private String email;

    private String password;

    private LocalDate fechaNacimiento;

    private String role;
}
