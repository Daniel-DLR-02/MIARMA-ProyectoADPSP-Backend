package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.repository.PostRepository;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.PostService;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioDtoConverter {

    private final UsuarioRepository repository;
    private final PostRepository postRepository;

    public GetUsuarioDto usuarioToGetUsuarioDto(Usuario user){
        return GetUsuarioDto.builder()
                .nick(user.getNick())
                .nombre(user.getNombre())
                .fechaDeNacimiento(user.getFechaNacimiento())
                .numeroSeguidores(repository.findFollowers(user.getId()).size())
                .numeroSeguidos(user.getFollows().size())
                .numeroPublicaciones(postRepository.findCurrentUserPostsWithId(Pageable.unpaged(),user.getId()).getSize())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .perfilPublico(user.isPerfilPublico())
                .build();

    }
}
