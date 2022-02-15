package com.salesianos.dam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;



@NamedEntityGraph(
        name = "grafo-post-usuario",
        attributeNodes = {
                @NamedAttributeNode("usuario")
        }
        )
@Entity
@Table(name="post")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    private Long id;

    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String texto;

    private String imagenAdjunta;

    private String imagenAdjuntaResized;

    private boolean publica;
}
