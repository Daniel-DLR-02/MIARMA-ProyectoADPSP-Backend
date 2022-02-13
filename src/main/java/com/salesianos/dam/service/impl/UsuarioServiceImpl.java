package com.salesianos.dam.service.impl;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final StorageService storageService;

    @Override
    public Usuario save(CreateUsuarioDto createProductoDto, MultipartFile file) throws Exception {

        String filename = storageService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filename)
                .toUriString();

        return repository.save(Usuario.builder()
                .nombre(createProductoDto.getNombre())
                .nick(createProductoDto.getNick())
                .email(createProductoDto.getEmail())
                .fechaNacimiento(createProductoDto.getFechaNacimiento())
                .password(createProductoDto.getPassword1())
                .perfilPublico(createProductoDto.isPublico())
                .avatar(uri)
                .build());


    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }
}
