package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.validacion.simple.anotaciones.UniqueUserEmail;
import com.salesianos.dam.validacion.simple.anotaciones.UniqueUserNick;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Validated
public class CreateUsuarioDto {

    private String nombre;

    @UniqueUserNick
    private String nick;

    @UniqueUserEmail
    private String email;

    private String password;

    private LocalDate fechaNacimiento;

    private boolean publico;

    private String role;
}
