package com.salesianos.dam.controller;

import com.salesianos.dam.model.Usuario;
import com.salesianos.dam.model.dto.Peticion.GetPeticionDto;
import com.salesianos.dam.model.dto.Usuario.GetUsuarioDto;
import com.salesianos.dam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UsuarioService userService;

    @GetMapping("follow/list")
    public ResponseEntity<List<GetUsuarioDto>> getRequestList(@AuthenticationPrincipal Usuario currentUser){
        return ResponseEntity.status(HttpStatus.OK).body(userService.peticionesDelUsuarioActual(currentUser));
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
