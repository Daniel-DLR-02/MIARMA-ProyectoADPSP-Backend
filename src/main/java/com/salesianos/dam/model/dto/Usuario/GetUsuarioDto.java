package com.salesianos.dam.model.dto.Usuario;

import lombok.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class GetUsuarioDto {

    private String nick;
    private String email;
    private String avatar;
    private List<String> posts;
    private String perfilPublico;
}
