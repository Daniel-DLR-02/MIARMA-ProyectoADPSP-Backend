package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDtoConverter {

    public GetUsuarioDto usuarioToGetUsuarioDto(Usuario user){
        return GetUsuarioDto.builder()
                .nick(user.getNick())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .perfilPublico(user.isPerfilPublico())
                .build();
    }
}
