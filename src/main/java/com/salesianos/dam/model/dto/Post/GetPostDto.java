package com.salesianos.dam.model.dto.Post;

import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetPostDto {
    private Long id;
    private String titulo;
    private GetUsuarioDto usuario;
    private String texto;
    private String ficheroAdjunto;
    private String ficheroAdjuntoResized;
    private boolean publica;
}
