package com.salesianos.dam.controller;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Peticion.GetPeticionDto;
import com.salesianos.dam.model.dto.Usuario.CreateUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import com.salesianos.dam.model.dto.Usuario.UsuarioDtoConverter;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UsuarioService userService;
    private final UsuarioDtoConverter usuarioDtoConverter;

    @GetMapping("/profile/{id}")
    public ResponseEntity<GetUsuarioDto> getProfile(@PathVariable UUID id,
                                              @AuthenticationPrincipal Usuario currentUser) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsuarioById(id,currentUser));
    }

    @PutMapping("/profile/me")
    public ResponseEntity<GetUsuarioDto> edit(@RequestPart("file") MultipartFile file,
                                           @RequestPart("post") @Valid CreateUsuarioDto editedUser,
                                           @AuthenticationPrincipal Usuario currentUser) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(usuarioDtoConverter.usuarioToGetUsuarioDto(userService.edit(currentUser,editedUser,file)));

    }

    @GetMapping("follow/list")
    public ResponseEntity<?> getRequestList(@PageableDefault(size = 10,page=0) Pageable pageable,
                                            @AuthenticationPrincipal Usuario currentUser){
        return ResponseEntity.ok(userService.peticionesDelUsuarioActual(pageable,currentUser));
    }

    @PostMapping("follow/{nick}")
    public ResponseEntity<GetPeticionDto> createFollowrequest(@AuthenticationPrincipal Usuario currentUser,
                                                              @PathVariable String nick){
        return ResponseEntity.status(HttpStatus.OK).body(userService.createFollowRequest(currentUser,nick));
    }

    @PostMapping("follow/accept/{id}")
    public ResponseEntity<GetUsuarioDto> acceptFollowRequest(@AuthenticationPrincipal Usuario currentUser,
                                                             @PathVariable Long id){
        userService.acceptFollowRequest(currentUser,id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("follow/decline/{id}")
    public ResponseEntity<GetUsuarioDto> declineFollowRequest(@AuthenticationPrincipal Usuario currentUser,
                                                              @PathVariable Long id){
        userService.declineFollowRequest(currentUser,id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
