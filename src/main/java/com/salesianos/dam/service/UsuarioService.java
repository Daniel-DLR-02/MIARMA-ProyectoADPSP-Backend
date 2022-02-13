package com.salesianos.dam.service;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UsuarioService {
    Usuario save(CreateUsuarioDto createProductoDto, MultipartFile file) throws Exception;
    List<Usuario> findAll();
}