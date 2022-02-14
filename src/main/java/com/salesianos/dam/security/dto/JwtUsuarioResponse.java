package com.salesianos.dam.security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtUsuarioResponse {
    private String email;
    private String nick;
    private String avatar;
    private String role;
    private String token;
}

