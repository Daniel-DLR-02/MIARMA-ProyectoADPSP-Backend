package com.salesianos.dam.model.dto.Usuario;

import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUsuarioDto {

    private String nombre;
    private String nick;
    private String email;
    private String password;
    private LocalDate fechaNacimiento;
    private boolean publico;
    private String role;
}
