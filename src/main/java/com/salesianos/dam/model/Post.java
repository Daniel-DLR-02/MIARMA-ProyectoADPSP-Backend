package com.salesianos.dam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String titulo;

    @ManyToOne
    private Usuario usuario;

    private String texto;

    private String ficheroAdjunto;

    private String ficheroAdjuntoResized;

    private boolean publica;
}
