package com.salesianos.dam.controller;

import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.service.StorageService;
import com.salesianos.dam.service.UsuarioService;
import com.salesianos.dam.utils.MediaTypeUrlResource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController{

    private final StorageService storageService;
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

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        MediaTypeUrlResource resource = (MediaTypeUrlResource) storageService.loadAsResource(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header("content-type", resource.getType())
                .body(resource);


    }
}
