package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class GetUsuarioDto {

    private String nick;
    private String nombre;
    private LocalDate fechaDeNacimiento;
    private int numeroSeguidores;
    private int numeroSeguidos;
    private int numeroPublicaciones;
    private String email;
    private String avatar;
    private boolean perfilPublico;
}
