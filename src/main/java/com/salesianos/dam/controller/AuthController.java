package com.salesianos.dam.controller;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.UsuarioDtoConverter;
import com.salesianos.dam.security.dto.JwtUsuarioResponse;
import com.salesianos.dam.security.dto.LoginDto;
import com.salesianos.dam.security.jwt.JwtProvider;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsuarioService userService;
    private final UsuarioDtoConverter usuarioDtoConverter;

    @GetMapping("/me")
    public ResponseEntity<GetUsuarioDto> whoami(@AuthenticationPrincipal Usuario currentUser){
        return ResponseEntity.ok(usuarioDtoConverter.usuarioToGetUsuarioDto(currentUser));
    }

    private JwtUsuarioResponse convertUserToJwtUserResponse(Usuario user, String jwt) {
        return JwtUsuarioResponse.builder()
                .nick(user.getNick())
                .email(user.getEmail())
                .role(user.getRole().name())
                .avatar(user.getAvatar())
                .token(jwt)
                .build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<GetUsuarioDto> create(@RequestPart("file") MultipartFile file,
                                                @RequestPart("user") CreateUsuarioDto newUsuario) throws Exception {

        Usuario saved = userService.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDtoConverter.usuarioToGetUsuarioDto(saved));



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

}
