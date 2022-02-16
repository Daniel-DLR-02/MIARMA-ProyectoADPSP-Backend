package com.salesianos.dam.service.impl;

import com.salesianos.dam.model.UserRole;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.repository.UsuarioRepository;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository repository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario save(CreateUsuarioDto createUsuarioDto, MultipartFile file) throws Exception {

        String filenameResized = storageService.storeResized(file,128);


        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(filenameResized)
                .toUriString();

        return repository.save(Usuario.builder()
                .nombre(createUsuarioDto.getNombre())
                .nick(createUsuarioDto.getNick())
                .email(createUsuarioDto.getEmail())
                .fechaNacimiento(createUsuarioDto.getFechaNacimiento())
                .password(passwordEncoder.encode(createUsuarioDto.getPassword()))
                .perfilPublico(createUsuarioDto.isPublico())
                .avatar(uri)
                .role(UserRole.USER)
                .build());


    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> findById(UUID userId) {
        return repository.findById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repository.findFirstByNick(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick+ "no encontrado"));
    }
}
