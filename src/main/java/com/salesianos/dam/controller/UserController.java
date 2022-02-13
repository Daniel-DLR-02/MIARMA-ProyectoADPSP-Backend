package com.salesianos.dam.controller;

import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController{

    private final UsuarioService service;

    @PostMapping("/auth/register")
    public ResponseEntity<?> create(@RequestPart("file") MultipartFile file,
                                    @RequestPart("user") CreateUsuarioDto newUsuario) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(newUsuario, file));


    }

    @GetMapping("/")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.findAll());
    }

}
