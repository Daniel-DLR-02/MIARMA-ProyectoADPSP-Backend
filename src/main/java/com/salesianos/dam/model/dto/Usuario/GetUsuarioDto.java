package com.salesianos.dam.model.dto.Usuario;

import com.salesianos.dam.model.Post;
import com.salesianos.dam.model.Usuario;
import lombok.*;

import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class GetUsuarioDto {

    private String nick;
    private String email;
    private String avatar;
    private String resizedAvatar;
    private boolean perfilPublico;
}
