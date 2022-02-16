package com.salesianos.dam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID idSeguidor;

    @ManyToOne
    private Usuario usuario;

    @PreRemove
    public void borrarSolicitud(){
        usuario.getFollow_request().remove(this);
        this.setUsuario(null);
    }

}
