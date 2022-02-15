package com.salesianos.dam.model.dto.Post;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.UsuarioDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class PostDtoConverter {

    UsuarioDtoConverter userDtoConverter;

    /*
    public Post createPostDtoToPost(CreatePostDto createPostDto, Usuario createdBy){
        return Post.builder()
                .usuario(createdBy)
                .titulo(createPostDto.getTitulo())
                .texto(createPostDto.getTexto())
                .publica(createPostDto.isPublica())
                .build();
    }
    */

    public GetPostDto postToGetPostDto(Post post){
        return GetPostDto.builder()
                .titulo(post.getTitulo())
                .usuario(userDtoConverter.usuarioToGetUsuarioDto(post.getUsuario()))
                .texto(post.getTexto())
                .ficheroAdjunto(post.getFicheroAdjunto())
                .ficheroAdjuntoResized(post.getFicheroAdjuntoResized())
                .publica(post.isPublica())
                .build();
    }

}
