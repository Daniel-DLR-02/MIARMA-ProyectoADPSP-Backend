package com.salesianos.dam.controller;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.security.dto.JwtUsuarioResponse;
import com.salesianos.dam.security.dto.LoginDto;
import com.salesianos.dam.security.jwt.JwtProvider;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.service.UsuarioService;
import com.salesianos.dam.utils.MediaTypeUrlResource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController{

    private final StorageService storageService;
    private final UsuarioService service;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/auth/register")
    public ResponseEntity<Usuario> create(@RequestPart("file") MultipartFile file,
                                    @RequestPart("user") CreateUsuarioDto newUsuario) throws Exception {

        Usuario saved = service.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(saved);
            //return ResponseEntity.httpStatus(HttpStatus.OK).build(usuarioDtoConverter.usuarioToGetUsuarioDto(saved));

        //return ResponseEntity.status(HttpStatus.CREATED)
        //        .body(service.save(newUsuario, file));


    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getNick(),
                                loginDto.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwt = jwtProvider.generateToken(authentication);


        Usuario user = (Usuario) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertUserToJwtUserResponse(user, jwt));

    }

    private JwtUsuarioResponse convertUserToJwtUserResponse(Usuario user, String jwt) {
        return JwtUsuarioResponse.builder()
                .nick(user.getNick())
                .email(user.getEmail())
                .role(user.getRole().name())
                .avatar(user.getAvatarResized())
                .token(jwt)
                .build();
    }

    @GetMapping("/")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        MediaTypeUrlResource resource = (MediaTypeUrlResource) storageService.loadAsResource(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header("content-type", resource.getType())
                .body(resource);


    }
}
