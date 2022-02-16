package com.salesianos.dam.service.impl;

import com.salesianos.dam.exception.RequestNotFoundException;
import com.salesianos.dam.exception.UnauthorizedRequestException;
import com.salesianos.dam.exception.UserNotFoundException;
import com.salesianos.dam.model.SolicitudSeguimiento;
import com.salesianos.dam.model.UserRole;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Peticion.GetPeticionDto;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import com.salesianos.dam.repository.SolicitudSeguimientoRepository;
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
    private final SolicitudSeguimientoRepository requestRepos;

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
    public GetPeticionDto createFollowRequest(Usuario currentUser, String nick) {

        Optional<Usuario> usuarioReclamado = repository.findByNick(nick);

            if(usuarioReclamado.isPresent()) {
                SolicitudSeguimiento followRequest = SolicitudSeguimiento.builder()
                        .usuario(usuarioReclamado.get())
                        .build();
                requestRepos.save(followRequest);
                repository.save(currentUser);
                return GetPeticionDto.builder()
                        .idSolicitud(followRequest.getId())
                        .idSeguidor(currentUser.getId())
                        .idSeguido(usuarioReclamado.get().getId())
                        .build();
            }
            else{
                throw new UserNotFoundException("Usuario reclamado no encontrado.");
            }
    }

    @Override
    public Usuario acceptFollowRequest(Usuario currentUser, Long idRequest) {
        if(requestRepos.findById(idRequest).isPresent()){
            if(requestRepos.findById(idRequest).get().getUsuario().getId().equals(currentUser.getId())){
                requestRepos.deleteById(idRequest);
                currentUser.getFollows().add(currentUser);
                return repository.save(currentUser);
            }else{
                throw new UnauthorizedRequestException("Esta petición de seguimiento no pertenece al perfil logueado.") ;
            }
        }
        else{
            throw new RequestNotFoundException("Petición de seguimiento no encontrada");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.repository.findFirstByNick(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick+ "no encontrado"));
    }
}
