package com.salesianos.dam.model.dto.Post;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.UsuarioDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDtoConverter {

    private final UsuarioDtoConverter userDtoConverter;


    public GetPostDto postToGetPostDto(Post post){
        return GetPostDto.builder()
                .id(post.getId())
                .titulo(post.getTitulo())
                .usuario(userDtoConverter.usuarioToGetUsuarioDto(post.getUsuario()))
                .texto(post.getTexto())
                .ficheroAdjunto(post.getFicheroAdjunto())
                .ficheroAdjuntoResized(post.getFicheroAdjuntoResized())
                .publica(post.isPublica())
                .build();
    }

}
