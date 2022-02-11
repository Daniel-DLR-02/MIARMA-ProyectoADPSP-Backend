package com.salesianos.dam.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private UUID id;

    private String name;
    private String nick;
    private String email;
    private LocalDate fechaNacimiento;
    private String fotoPerfil;
    private boolean perfilPublico;


}
