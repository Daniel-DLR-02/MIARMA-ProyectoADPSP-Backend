package com.salesianos.dam.service;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {
    Usuario save(CreateUsuarioDto createUsuarioDto, MultipartFile file) throws Exception;
    List<Usuario> findAll();

    Optional<Usuario> findById(UUID userId);
}