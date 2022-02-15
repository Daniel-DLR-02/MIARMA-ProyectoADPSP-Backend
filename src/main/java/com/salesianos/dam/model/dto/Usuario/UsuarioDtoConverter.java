package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDtoConverter {

    public GetUsuarioDto usuarioToGetUsuarioDto(Usuario user){
        return GetUsuarioDto.builder()

                .build();
    }
}
