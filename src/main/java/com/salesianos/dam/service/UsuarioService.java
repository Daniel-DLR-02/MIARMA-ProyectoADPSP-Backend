package com.salesianos.dam.service;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Peticion.GetPeticionDto;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {

    Usuario save(CreateUsuarioDto createUsuarioDto, MultipartFile file) throws Exception;

    List<Usuario> findAll();

    Optional<Usuario> findById(UUID userId);

    GetPeticionDto createFollowRequest(Usuario currentUser, String nick);

    Usuario acceptFollowRequest(Usuario currentUser,Long idRequest);
}